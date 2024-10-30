package com.juny.simplehttpparser;

import static com.juny.simplehttpparser.ErrorMessageConst.HTTP_MULTIPART_NEED_BOUNDARY_MSG;
import static com.juny.simplehttpparser.HttpConst.ASSIGN_SYMBOL;
import static com.juny.simplehttpparser.HttpConst.HTTP_BOUNDARY;
import static com.juny.simplehttpparser.HttpConst.HTTP_CONTENT_DISPOSITION;
import static com.juny.simplehttpparser.HttpConst.HTTP_CONTENT_DISPOSITION_FILENAME;
import static com.juny.simplehttpparser.HttpConst.HTTP_CONTENT_DISPOSITION_NAME;
import static com.juny.simplehttpparser.HttpConst.HTTP_ORIGINAL_FILENAME;
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

  public MyMultipartRequest parse(File multipartData) throws IOException {

    MyMultipartRequest multipartRequest = new MyMultipartRequest();

    try (BufferedReader br = new BufferedReader(new FileReader(multipartData))) {
      parseHeader(multipartRequest, br);
      parseMultipartBody(multipartRequest, br);
    }

    return multipartRequest;
  }

  public void parseHeader(MyMultipartRequest multipartRequest, BufferedReader br) throws IOException {

    boolean isFirstLine = true;

    while (true) {

      String line = br.readLine();

      if (line.isEmpty()) {
        break;
      }

      if (isFirstLine) {
        isFirstLine = parseRequestLine(line);
        continue;
      }

      String[] keyValues = line.split(SPACE_SYMBOL);

      if (keyValues[0].startsWith(HttpHeaders.CONTENT_TYPE)) {
        parseContentType(keyValues);
        continue;
      }
      requestInfos.put(keyValues[0].substring(0, keyValues[0].length() - 1), keyValues[1]);
    }

    for (var e : requestInfos.entrySet()) {
      multipartRequest.getHeaders().put(e.getKey(), e.getValue());
    }
  }

  private boolean parseRequestLine(String line) {

    String[] values = line.split(SPACE_SYMBOL);

    requestInfos.put(HTTP_REQUEST_LINE_METHOD, values[0]);
    requestInfos.put(HTTP_REQUEST_LINE_REQUEST_URI, values[1]);
    requestInfos.put(HTTP_REQUEST_LINE_PROTOCOL, values[2]);

    return false;
  }

  public void parseMultipartBody(MyMultipartRequest multipartRequest, BufferedReader br)
    throws IOException {

    List<MultipartFile> multipartFiles = new ArrayList<>();
    String name = null;
    String originalFilename = null;
    String contentType = null;
    boolean hasContent = false;
    StringBuilder content = new StringBuilder();
    String boundary = getBoundary();

    if (boundary == null) {
      throw new RuntimeException(HTTP_MULTIPART_NEED_BOUNDARY_MSG);
    }
    
    while (true) {

      String line = br.readLine();

      if (Objects.isNull(line)) {
        break;
      }

      if (line.startsWith(boundary)) {
        if (!content.isEmpty()) {
          hasContent = isFileAdded(name, originalFilename, contentType, content, multipartFiles);
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

        Map<String, String> ret = extractNameAndFileName(line);
        name = ret.getOrDefault(HTTP_CONTENT_DISPOSITION_NAME, name);
        originalFilename = ret.getOrDefault(HTTP_ORIGINAL_FILENAME, originalFilename);
        continue;
      }

      if (line.startsWith(HttpHeaders.CONTENT_TYPE)) {
        contentType = line.split(SPACE_SYMBOL)[1];
        continue;
      }

      if (line.isEmpty()) {
        hasContent = true;
        continue;
      }

      if (hasContent) {
        content.append(line);
      }
    }

    for (var e : multipartFiles) {

      multipartRequest.getMultipartFiles().put(e.getName(), e);
    }
  }

  private String getBoundary() {

    String boundary = null;
    if (requestInfos.containsKey(HTTP_BOUNDARY)) {
      boundary = "--" + requestInfos.get(HTTP_BOUNDARY);
    }

    if (Objects.isNull(boundary)) {
      throw new RuntimeException(HTTP_MULTIPART_NEED_BOUNDARY_MSG);
    }

    return boundary;
  }

  private static boolean isFileAdded(String name, String originalFilename, String contentType,
    StringBuilder content, List<MultipartFile> multipartFiles) {

    MockMultipartFile mockMultipartFile = new MockMultipartFile(name, originalFilename,
      contentType, content.toString().getBytes());
    multipartFiles.add(mockMultipartFile);
    content.setLength(0);

    return false;
  }

  private void parseContentType(String[] keyValues) {

    requestInfos.put(HttpHeaders.CONTENT_TYPE, keyValues[1].substring(0, keyValues[1].length() - 1));

    for (int i = 2; i < keyValues.length; ++i) {
      if (keyValues[i].contains(ASSIGN_SYMBOL)) {
        String[] keyValue = keyValues[i].split(ASSIGN_SYMBOL);
        requestInfos.put(keyValue[0], keyValue[1]);
      }
    }
  }

  private Map<String, String> extractNameAndFileName(String line) {

    Map<String, String> ret = new HashMap<>();

    String[] keyValues = line.split(SPACE_SYMBOL);
    for (int i = 1; i < keyValues.length; ++i) {
      String[] keyValue = keyValues[i].split(ASSIGN_SYMBOL);
      if (keyValue[0].equals(HTTP_CONTENT_DISPOSITION_NAME)) {
        ret.put(HTTP_CONTENT_DISPOSITION_NAME, keyValue[1].substring(0, keyValue[1].length() - 1));
      } else if (keyValue[0].equals(HTTP_CONTENT_DISPOSITION_FILENAME)) {
        ret.put(HTTP_ORIGINAL_FILENAME, keyValue[1]);
      }
    }
    return ret;
  }
}
