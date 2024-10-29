package com.juny.simplehttpparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public class MyMultipartRequest {

  private List<Map<String, String>> headers = new ArrayList<Map<String, String>>();
  private Map<String, MultipartFile> multipartFiles = new HashMap<String, MultipartFile>();

  public String getMethod() {

    for (var e : headers) {
      if (e.containsKey("method")) {
        return e.get("method");
      }
    }
    return null;
  }

  public String getURI() {

    for (var e : headers) {
      if (e.containsKey("requestURI")) {
        return e.get("requestURI");
      }
    }
    return null;
  }

  public String getProtocol() {

    for (var e : headers) {
      if (e.containsKey("protocol")) {
        return e.get("protocol");
      }
    }
    return null;
  }

  public String getContentLength() {

    for (var e : headers) {
      if (e.containsKey("Content-Length")) {
        return e.get("Content-Length");
      }
    }
    return null;
  }

  public String getBoundary() {

    for (var e : headers) {
      if (e.containsKey("boundary")) {
        return e.get("boundary");
      }
    }
    return null;
  }


  public String getContentType() {
    for (var e : headers) {
      if (e.containsKey("Content-Type")) {
        return e.get("Content-Type");
      }
    }
    return null;
  }

  public String getHost() {
    for (var e : headers) {
      if (e.containsKey("Host")) {
        return e.get("Host");
      }
    }
    return null;
  }

  public String getConnection() {
    for (var e : headers) {
      if (e.containsKey("Connection")) {
        return e.get("Connection");
      }
    }
    return null;
  }

  public String getHeader(String key) {

    for (var e : headers) {
      if (e.containsKey(key)) {
        return e.get(key);
      }
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

   headers.add(header);
  }
}
