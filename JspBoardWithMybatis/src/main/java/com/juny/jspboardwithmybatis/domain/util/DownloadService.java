package com.juny.jspboardwithmybatis.domain.util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

  public void responseFile(String path, HttpServletResponse res) {

    Path filePath = Paths.get(path);

    if (!Files.exists(filePath)) {
      throw new RuntimeException("File not found: " + path);
    }

    res.setContentType("application/octet-stream");
    res.setHeader("Content-Disposition", "attachment; filename=\"" + filePath.getFileName() + "\"");

    try (InputStream inputStream = Files.newInputStream(filePath);
        OutputStream outputStream = res.getOutputStream()) {

      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error while processing file: " + e.getMessage(), e);
    }
  }
}
