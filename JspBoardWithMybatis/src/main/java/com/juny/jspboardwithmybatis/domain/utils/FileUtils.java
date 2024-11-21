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
import java.util.stream.IntStream;
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

  /**
   *
   *
   * <h1>파일 세부 정보 저장</h1>
   *
   * - MultipartFile 파일이 없어도 빈 객체로 전달
   *
   * @param files
   * @param path
   * @return FileDetails
   */
  public static List<FileDetails> parseFileDetails(List<MultipartFile> files, String path) {

    String storedPath = "/Users/jijunhyuk/JunyProjects/ebrainsoft/" + path + "/";
    List<FileDetails> fileDetailsList = new ArrayList<>();

    for (var file : files) {

      if (file.isEmpty()) {
        continue;
      }

      String extension = "";
      String fileName = file.getOriginalFilename();
      String storedName = UUID.randomUUID().toString().replace("-", "");
      if (fileName.contains(".")) {
        extension = fileName.substring(fileName.lastIndexOf(".") + 1);
      }
      fileDetailsList.add(
          new FileDetails(fileName, storedName, storedPath, extension, file.getSize()));
    }
    return fileDetailsList;
  }

  /**
   *
   *
   * <h1>파일 시스템에 파일 저장</h1>
   *
   * <br>
   * - MultipartFile 파일이 없어도 빈 객체로 전달
   *
   * @param files
   * @param fileDetailsList
   */
  public static void saveFile(List<MultipartFile> files, List<FileDetails> fileDetailsList) {

    IntStream.range(0, fileDetailsList.size())
        .forEach(
            idx -> {
              var file = files.get(idx);
              var fileDetails = fileDetailsList.get(idx);
              String extension = fileDetails.getExtension();
              String storedName = fileDetails.getStoredName();
              String storedPath = fileDetails.getStoredPath();
              String delimiter = ".";
              if (fileDetails.getExtension().equals("")) delimiter = "";

              try {
                file.transferTo(Paths.get(storedPath, storedName + delimiter + extension));
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });
  }

  public static void deleteFile(List<String> deleteFilePaths) {

    for (var path : deleteFilePaths) {
      try {
        Files.delete(Path.of(path));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
