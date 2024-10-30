package com.juny.simplehttpparser;

import static com.juny.simplehttpparser.ErrorMessageConst.HTTP_MULTIPART_NEED_BOUNDARY_MSG;
import static com.juny.simplehttpparser.HttpConst.ASSIGN_SYMBOL;
import static com.juny.simplehttpparser.HttpConst.HTTP_BOUNDARY;
import static com.juny.simplehttpparser.HttpConst.HTTP_CONTENT_DISPOSITION;
import static com.juny.simplehttpparser.HttpConst.HTTP_CONTENT_DISPOSITION_FILENAME;
import static com.juny.simplehttpparser.HttpConst.HTTP_CONTENT_DISPOSITION_NAME;
import static com.juny.simplehttpparser.HttpConst.HTTP_REQUEST_LINE_METHOD;
import static com.juny.simplehttpparser.HttpConst.HTTP_REQUEST_LINE_PROTOCOL;
import static com.juny.simplehttpparser.HttpConst.HTTP_REQUEST_LINE_REQUEST_URI;
import static com.juny.simplehttpparser.HttpConst.SPACE_SYMBOL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class SimpleParser {

  private final Map<String, String> requestInfos = new HashMap<>();

  public void parseMultipartBody(MyMultipartRequest multipartRequest, BufferedReader br)
    throws IOException {

    List<MultipartFile> multipartFiles = new ArrayList<>();

    String boundary = null;
    if (requestInfos.containsKey(HTTP_BOUNDARY)) {
      boundary = "--" + requestInfos.get(HTTP_BOUNDARY);
    }

    if (Objects.isNull(boundary)) {
      throw new RuntimeException(HTTP_MULTIPART_NEED_BOUNDARY_MSG);
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

      if (line.startsWith(HTTP_CONTENT_DISPOSITION)) {
        String[] keyValues = line.split(SPACE_SYMBOL);

        for (int i = 1; i < keyValues.length; ++i) {
          String[] keyValue = keyValues[i].split(ASSIGN_SYMBOL);
          if (keyValue[0].equals(HTTP_CONTENT_DISPOSITION_NAME)) {
            name = keyValue[1].substring(0, keyValue[1].length() - 1);
          }
          else if (keyValue[0].equals(HTTP_CONTENT_DISPOSITION_FILENAME)) {
            originalFilename = keyValue[1];
          }
        }
        continue;
      }

      if (line.startsWith(HttpHeaders.CONTENT_TYPE)) {
        contentType = line.split(SPACE_SYMBOL)[1];
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

      multipartRequest.getMultipartFiles().put(e.getName(), e);
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
        String[] values = line.split(SPACE_SYMBOL);
        requestInfos.put(HTTP_REQUEST_LINE_METHOD, values[0]);
        requestInfos.put(HTTP_REQUEST_LINE_REQUEST_URI, values[1]);
        requestInfos.put(HTTP_REQUEST_LINE_PROTOCOL, values[2]);
        isFirstLine = false;
        continue;
      }

      String[] keyValues = line.split(SPACE_SYMBOL);

      if (keyValues[0].startsWith(HttpHeaders.CONTENT_TYPE)) {
        requestInfos.put(HttpHeaders.CONTENT_TYPE, keyValues[1].substring(0, keyValues[1].length() - 1));
        for (int i = 2; i < keyValues.length; ++i) {
          if (keyValues[i].contains(ASSIGN_SYMBOL)) {
            String[] keyValue = keyValues[i].split(ASSIGN_SYMBOL);
            requestInfos.put(keyValue[0], keyValue[1]);
          }
        }
        continue;
      }
      requestInfos.put(keyValues[0].substring(0, keyValues[0].length() - 1), keyValues[1]);
    }

    for (var e : requestInfos.entrySet()) {
      multipartRequest.getHeaders().put(e.getKey(), e.getValue());
    }
  }

  public MyMultipartRequest parse(File multipartData) throws IOException {

    MyMultipartRequest multipartRequest = new MyMultipartRequest();

    try (BufferedReader br = new BufferedReader(new FileReader(multipartData))) {
      parseHeader(multipartRequest, br);
      parseMultipartBody(multipartRequest, br);
    }

    return multipartRequest;
  }
}
