package com.juny.simplehttpparser;

import static com.juny.simplehttpparser.HttpConst.HTTP_BOUNDARY;
import static com.juny.simplehttpparser.HttpConst.HTTP_REQUEST_LINE_METHOD;
import static com.juny.simplehttpparser.HttpConst.HTTP_REQUEST_LINE_PROTOCOL;
import static com.juny.simplehttpparser.HttpConst.HTTP_REQUEST_LINE_REQUEST_URI;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

public class MyMultipartRequest {

  private Map<String, String> headers = new HashMap<>();
  private Map<String, MultipartFile> multipartFiles = new HashMap<>();

  public String getMethod() {

    if (headers.containsKey(HTTP_REQUEST_LINE_METHOD)) {
      return headers.get(HTTP_REQUEST_LINE_METHOD);
    }
    return null;
  }

  public String getURI() {

    if (headers.containsKey(HTTP_REQUEST_LINE_REQUEST_URI)) {
      return headers.get(HTTP_REQUEST_LINE_REQUEST_URI);
    }
    return null;
  }

  public String getProtocol() {

    if (headers.containsKey(HTTP_REQUEST_LINE_PROTOCOL)) {
      return headers.get(HTTP_REQUEST_LINE_PROTOCOL);
    }
    return null;
  }

  public String getContentLength() {

    if (headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
      return headers.get(HttpHeaders.CONTENT_LENGTH);
    }
    return null;
  }

  public String getBoundary() {

    if (headers.containsKey(HTTP_BOUNDARY)) {
      return headers.get(HTTP_BOUNDARY);
    }
    return null;
  }


  public String getContentType() {

    if (headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
      return headers.get(HttpHeaders.CONTENT_TYPE);
    }
    return null;
  }

  public String getHost() {

    if (headers.containsKey(HttpHeaders.HOST)) {
      return headers.get(HttpHeaders.HOST);
    }
    return null;
  }

  public String getConnection() {

    if (headers.containsKey(HttpHeaders.CONNECTION)) {
      return headers.get(HttpHeaders.CONNECTION);
    }
    return null;
  }

  public String getHeader(String key) {

    if (headers.containsKey(key)) {
      return headers.get(key);
    }
    return null;
  }

  public MultipartFile getMultipartFile(String key) {

    if (multipartFiles.containsKey(key)) {
      return multipartFiles.get(key);
    }
    return null;
  }

  public Map<String, MultipartFile> getMultipartFiles() {

    return multipartFiles;
  }

  public Map<String, String> getHeaders() {

    return headers;
  }
}
