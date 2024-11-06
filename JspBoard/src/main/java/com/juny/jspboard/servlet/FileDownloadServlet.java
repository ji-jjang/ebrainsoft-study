package com.juny.jspboard.servlet;

import com.juny.jspboard.constant.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileDownloadServlet implements BoardControllerServlet {

  private final int BUFFER_SIZE = 4096;

  // TODO 상수처리
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    String filePath = req.getParameter("filePath");
    String fileName = req.getParameter("fileName") + req.getParameter("extension");

    File file = new File(filePath, fileName);
    if (!file.exists()) {
      res.sendError(HttpServletResponse.SC_NOT_FOUND, ErrorMessage.FILE_NOT_FOUND_MSG + filePath + fileName);
      return;
    }

    res.setContentType("application/octet-stream");
    res.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

    try (FileInputStream fileInputStream = new FileInputStream(file);
      OutputStream outputStream = res.getOutputStream()) {

      byte[] buffer = new byte[BUFFER_SIZE];
      int bytesRead;
      while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      throw new ServletException(ErrorMessage.FILE_DOWNLOAD_FAIL_MSG, e);
    }
  }
}
