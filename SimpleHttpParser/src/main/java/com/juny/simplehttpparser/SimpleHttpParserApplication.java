package com.juny.simplehttpparser;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class SimpleHttpParserApplication {

  private static final String FILE_PATH = "/Users/jijunhyuk/JunyProjects/ebrainsoft/SimpleHttpParser/";
  private static final String INPUT_FILE_NAME = "request-dummy.txt";
  private static final String FIRST_OUTPUT_FILE_NAME = "first.txt";
  private static final String SECOND_OUTPUT_FILE_NAME = "second.txt";

  public static void main(String[] args) throws IOException {

    File multipartData = new File(FILE_PATH + INPUT_FILE_NAME);
    SimpleParser simpleParser = new SimpleParser();

    MyMultipartRequest myMultiPartRequest = simpleParser.parse(multipartData);

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

    MultipartFileHelper.store(firstFile, FILE_PATH + FIRST_OUTPUT_FILE_NAME);

    MultipartFile secondFile = myMultiPartRequest.getMultipartFile("text2");
    System.out.println(secondFile.getName()); // text2
    System.out.println(secondFile.getContentType()); // application/octet-stream
    System.out.println(new String(secondFile.getBytes())); // This is second file.

    MultipartFileHelper.store(secondFile, FILE_PATH + SECOND_OUTPUT_FILE_NAME);
  }
}
