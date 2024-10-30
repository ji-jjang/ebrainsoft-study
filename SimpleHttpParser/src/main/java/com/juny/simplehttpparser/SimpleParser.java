package com.juny.simplehttpparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class SimpleParser {

  private final Map<String, String> requestInfos = new HashMap<>();

  public void addKeyValues(String key, String values) {

    requestInfos.put(key, values);
  }

  public void parseMultipartBody(MyMultipartRequest multipartRequest, BufferedReader br)
    throws IOException {

    List<MultipartFile> multipartFiles = new ArrayList<>();

    String boundary = null;
    if (requestInfos.containsKey("boundary")) {
      boundary = "--" + requestInfos.get("boundary");
    }

    if (Objects.isNull(boundary)) {
      throw new RuntimeException("boundary field is necessary.");
    }

    String name = null;
    String originalFilename = null;
    String contentType = null;
    boolean isContent = false;
    StringBuilder content = new StringBuilder();

    while (true) {

      String line = br.readLine();

      if (Objects.isNull(line)) {
        break;
      }

      if (!Objects.isNull(boundary) && line.startsWith(boundary)) {

        if (!content.isEmpty()) {
          MockMultipartFile mockMultipartFile = new MockMultipartFile(name, originalFilename,
            contentType, content.toString().getBytes());
          multipartFiles.add(mockMultipartFile);
          content.setLength(0);
          isContent = false;
        }
        if (line.endsWith("--")) {
          break;
        }
        name = null;
        originalFilename = null;
        contentType = null;
        continue;
      }

      if (line.startsWith("Content-Disposition")) {
        String[] keyValues = line.split(" ");

        for (int i = 1; i < keyValues.length; ++i) {
          String[] keyValue = keyValues[i].split("=");
          if (keyValue[0].equals("name")) {
            name = keyValue[1].substring(0, keyValue[1].length() - 1);
          }
          else if (keyValue[0].equals("filename")) {
            originalFilename = keyValue[1];
          }
        }
        continue;
      }

      if (line.startsWith("Content-Type")) {
        contentType = line.split(" ")[1];
        continue;
      }

      if (line.isEmpty()) {
        isContent = true;
        continue;
      }

      if (isContent) {
        content.append(line);
      }
    }

    for (var e : multipartFiles) {
      multipartRequest.addMultipartFile(e);
    }
  }

  public void parseHeader(MyMultipartRequest multipartRequest, BufferedReader br) throws IOException {

    boolean isFirstLine = true;

    while (true) {

      String line = br.readLine();

      if (line.isEmpty()) {
        break;
      }

      if (isFirstLine) {
        String[] values = line.split(" ");
        addKeyValues("method", values[0]);
        addKeyValues("requestURI", values[1]);
        addKeyValues("protocol", values[2]);
        isFirstLine = false;
        continue;
      }

      String[] keyValues = line.split(" ");

      if (keyValues[0].startsWith("Content-Type")) {

        addKeyValues("Content-Type", keyValues[1].substring(0, keyValues[1].length() - 1));
        for (int i = 2; i < keyValues.length; ++i) {
          if (keyValues[i].contains("=")) {
            String[] keyValue = keyValues[i].split("=");
            addKeyValues(keyValue[0], keyValue[1]);
          }
        }
        continue;
      }
      addKeyValues(keyValues[0].substring(0, keyValues[0].length() - 1), keyValues[1]);
    }

    for (var e : requestInfos.entrySet()) {
      multipartRequest.getHeaders().put(e.getKey(), e.getValue());
    }
  }

  public MyMultipartRequest parse(File multipartData) throws IOException {

    MyMultipartRequest multipartRequest = new MyMultipartRequest();
    BufferedReader br = new BufferedReader(new FileReader(multipartData));

    parseHeader(multipartRequest, br);
    parseMultipartBody(multipartRequest, br);

    br.close();
    return multipartRequest;
  }
}
