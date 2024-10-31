package com.juny.simplehttpparser;

import static com.juny.simplehttpparser.HttpConst.HTTP_ACCEPT_ENCODING;
import static com.juny.simplehttpparser.HttpConst.HTTP_USER_AGENT;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class SimpleHttpParserApplication {

  private static final String FILE_PATH = "/Users/jijunhyuk/JunyProjects/ebrainsoft/SimpleHttpParser/";
  private static final String INPUT_FILE_NAME = "request-dummy.txt";
  private static final String FIRST_OUTPUT_FILE_NAME = "first.txt";
  private static final String SECOND_OUTPUT_FILE_NAME = "second.txt";
  private static final String FIRST_FILE_NAME = "text1";
  private static final String SECOND_FILE_NAME = "text2";

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

    System.out.println(myMultiPartRequest.getHeader(HTTP_USER_AGENT)); // Apache-HttpClient/4.3.4
    System.out.println(myMultiPartRequest.getHeader(HTTP_ACCEPT_ENCODING)); // gzip,deflate

    MultipartFile firstFile = myMultiPartRequest.getMultipartFile(FIRST_FILE_NAME);
    System.out.println(firstFile.getName()); // text1
    System.out.println(firstFile.getContentType()); // application/octet-stream
    System.out.println(new String(firstFile.getBytes())); // This is first file.

    MultipartFileHelper.store(firstFile, FILE_PATH + FIRST_OUTPUT_FILE_NAME);

    MultipartFile secondFile = myMultiPartRequest.getMultipartFile(SECOND_FILE_NAME);
    System.out.println(secondFile.getName()); // text2
    System.out.println(secondFile.getContentType()); // application/octet-stream
    System.out.println(new String(secondFile.getBytes())); // This is second file.

    MultipartFileHelper.store(secondFile, FILE_PATH + SECOND_OUTPUT_FILE_NAME);
  }
}
