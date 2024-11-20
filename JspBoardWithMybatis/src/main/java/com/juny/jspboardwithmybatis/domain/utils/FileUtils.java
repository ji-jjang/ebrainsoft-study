package com.juny.jspboardwithmybatis.domain.utils;

import com.juny.jspboardwithmybatis.domain.utils.dto.FileDetails;
import com.juny.jspboardwithmybatis.domain.utils.dto.ResFileDownload;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

  /**
   *
   *
   * <h1>파일 다운로드 처리</h1>
   *
   * @param resFileDownload
   * @param res
   */
  public static void responseFile(ResFileDownload resFileDownload, HttpServletResponse res) {

    Path filePath = Paths.get(resFileDownload.getPath());

    if (!Files.exists(filePath)) {
      throw new RuntimeException("File not found: " + resFileDownload.getPath());
    }

    res.setContentType("application/octet-stream");
    res.setHeader(
        "Content-Disposition", "attachment; filename=\"" + resFileDownload.getLogicalName() + "\"");

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

  public static List<FileDetails> saveFileDetails(List<MultipartFile> files, String path) {

    String storedPath = "/Users/jijunhyuk/JunyProjects/ebrainsoft/" + path + "/";
    List<FileDetails> fileDetailsList = new ArrayList<>();

    for (var file : files) {
      String extension = "";
      String fileName = file.getOriginalFilename();
      String storedName = UUID.randomUUID().toString().replace("-", "");
      if (fileName.contains(".")) {
        extension = fileName.substring(fileName.lastIndexOf(".") + 1);
      }
      try {
        String delimiter = ".";
        if (extension.equals("")) delimiter = "";

        file.transferTo(Path.of(storedPath + storedName + delimiter + extension));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      fileDetailsList.add(
          new FileDetails(fileName, storedName, storedPath, extension, file.getSize()));
    }
    return fileDetailsList;
  }
}
