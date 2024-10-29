package com.juny.simplehttpparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public class MyMultipartRequest {

  private Map<String, String> headers = new HashMap<>();
  private Map<String, MultipartFile> multipartFiles = new HashMap<String, MultipartFile>();

  public String getMethod() {

    if (headers.containsKey("method")) {
      return headers.get("method");
    }
    return null;
  }

  public String getURI() {

    if (headers.containsKey("requestURI")) {
      return headers.get("requestURI");
    }
    return null;
  }

  public String getProtocol() {

    if (headers.containsKey("protocol")) {
      return headers.get("protocol");
    }
    return null;
  }

  public String getContentLength() {

    if (headers.containsKey("Content-Length")) {
      return headers.get("Content-Length");
    }
    return null;
  }

  public String getBoundary() {

    if (headers.containsKey("boundary")) {
      return headers.get("boundary");
    }
    return null;
  }


  public String getContentType() {

    if (headers.containsKey("Content-Type")) {
      return headers.get("Content-Type");
    }
    return null;
  }

  public String getHost() {

    if (headers.containsKey("Host")) {
      return headers.get("Host");
    }
    return null;
  }

  public String getConnection() {

    if (headers.containsKey("Connection")) {
      return headers.get("Connection");
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

  public void addMultipartFile(MultipartFile file) {

    multipartFiles.put(file.getName(), file);
  }

  public void addHeader(Map<String, String> header) {

   headers.putAll(header);
  }
}
