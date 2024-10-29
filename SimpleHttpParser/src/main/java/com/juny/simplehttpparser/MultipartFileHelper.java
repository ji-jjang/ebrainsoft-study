package com.juny.simplehttpparser;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileHelper {

  public static void store(MultipartFile multipartFile, String path) throws IOException {

    multipartFile.transferTo(new File(path));
  }
}
