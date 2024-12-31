package com.juny.finalboard.domain.post.common;

import com.juny.finalboard.domain.post.free.common.dto.FileDownloadVo;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileService {

  /**
   *
   *
   * <h1>파일 저장 </h1>
   *
   * @param attachments 멀티파트 파일
   * @param fileDetailsList 멀티파트 파일 정보
   */
  public void saveFile(List<MultipartFile> attachments, List<? extends FileDetails> fileDetailsList) {

    IntStream.range(0, fileDetailsList.size())
        .forEach(
            idx -> {
              var file = attachments.get(idx);

              FileDetails fileDetails = fileDetailsList.get(idx);
              try {
                file.transferTo(
                    Paths.get(
                        fileDetails.getStoredPath(),
                        fileDetails.getStoredName() + fileDetails.getExtension()));
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });
  }

  /**
   *
   *
   * <h1>파일 삭제 </h1>
   *
   * @param fileDetailsList 삭제할 파일 정보
   */
  public void deleteFile(List<? extends FileDetails> fileDetailsList) {

    for (var fileDetails : fileDetailsList) {
      String path =
          fileDetails.getStoredPath() + fileDetails.getStoredName() + fileDetails.getExtension();
      try {
        Files.delete(Path.of(path));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   *
   *
   * <h1>파일 다운로드 </h1>
   *
   * @param downloadVO FileDownloadVO
   * @param res HttpServletResponse
   */
  public void downloadFile(FileDownloadVo downloadVO, HttpServletResponse res) {

    Path filePath = Paths.get(downloadVO.storedPathAndFileName());

    if (!Files.exists(filePath)) {
      throw new RuntimeException(String.format("file not found, path: %s", filePath));
    }

    res.setContentType("application/octet-stream");
    res.setHeader(
        "Content-Disposition", "attachment; filename=\"" + downloadVO.logicalName() + "\"");

    try (InputStream inputStream = Files.newInputStream(filePath);
        OutputStream outputStream = res.getOutputStream()) {

      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
