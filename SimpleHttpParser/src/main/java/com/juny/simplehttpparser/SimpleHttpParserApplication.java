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

public class SimpleHttpParserApplication {

  private static final String FILE_PATH = "/Users/jijunhyuk/JunyProjects/ebrainsoft/SimpleHttpParser/";
  private static final String INPUT_FILE_NAME = "request-dummy.txt";
  private static final String OUTPUT_FILE_NAME = "first.txt";

  private static List<Map<String, String>> requestInfos = new ArrayList<>();
  private Map<String, MultipartFile> multipartFiles = new HashMap<String, MultipartFile>();

  public static void parseMultipartBody(MyMultipartRequest multipartRequest, BufferedReader br)
    throws IOException {

    List<MultipartFile> multipartFiles = new ArrayList<>();

    String boundary = null;
    for (var info : requestInfos) {
      if (info.containsKey("boundary")) {
        boundary = "--" + info.get("boundary");
        break;
      }
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

      if (line.startsWith(boundary)) {

        if (content.length() != 0) {
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
        System.out.println("empty!!");
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

  public static void parseHeader(MyMultipartRequest multipartRequest, BufferedReader br) throws IOException {

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

      System.out.println("line: " + line);
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

    for (var e : requestInfos) {
      multipartRequest.addHeader(e);
    }
  }

  public static MyMultipartRequest parse(File multipartData) throws IOException {

    MyMultipartRequest multipartRequest = new MyMultipartRequest();
    BufferedReader br = new BufferedReader(new FileReader(FILE_PATH + INPUT_FILE_NAME));

    parseHeader(multipartRequest, br);
    parseMultipartBody(multipartRequest, br);

    br.close();
    return multipartRequest;
  }

  public static void addKeyValues(String key, String values) {

    Map<String, String> map = new HashMap<>();
    map.put(key, values);

    requestInfos.add(map);
  }

  public static void main(String[] args) throws IOException {

    File multipartData = new File(FILE_PATH);
    MyMultipartRequest myMultiPartRequest = parse(multipartData);

    System.out.println(myMultiPartRequest.getMethod()); // POST
    System.out.println(myMultiPartRequest.getURI()); // /file/upload
    System.out.println(myMultiPartRequest.getProtocol()); // HTTP/1.1
    System.out.println(myMultiPartRequest.getContentLength()); // 344
    System.out.println(myMultiPartRequest.getContentType()); // multipart/form-data
    System.out.println(myMultiPartRequest.getBoundary()); // -----r1_eDOWu7FpA0LJdLwCMLJQapQGu
    System.out.println(myMultiPartRequest.getHost()); // localhost:8080
    System.out.println(myMultiPartRequest.getConnection()); // Keep-Alive

    System.out.println(myMultiPartRequest.getHeader("User-Agent")); // Apache-HttpClient/4.3.4
    System.out.println(myMultiPartRequest.getHeader("Accept-Encoding")); // gzip,deflate

    MultipartFile firstFile = myMultiPartRequest.getMultipartFile("text1");
    System.out.println(firstFile.getName()); // text1
    System.out.println(firstFile.getContentType()); // application/octet-stream
    System.out.println(new String(firstFile.getBytes())); // This is first file.

    MultipartFileHelper.store(firstFile, FILE_PATH + OUTPUT_FILE_NAME);
  }
}
