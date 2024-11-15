package com.juny.jspboard.global.servlet;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.Env;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String filename = req.getPathInfo().substring(1);

    File file = getFile(resp, filename);
    if (file == null) return;

    readAndWriteFile(resp, file);
  }

  private static File getFile(HttpServletResponse resp, String filename) throws IOException {
    File file = new File(Env.IMAGE_PATH, filename);
    if (!file.exists()) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return null;
    }
    return file;
  }

  private void readAndWriteFile(HttpServletResponse resp, File file) throws IOException {
    resp.setContentType(getServletContext().getMimeType(file.getName()));
    resp.setContentLength((int) file.length());
    try (FileInputStream in = new FileInputStream(file);
        OutputStream out = resp.getOutputStream()) {

      byte[] buffer = new byte[Constants.FILE_READ_BUFFER_SIZE];
      int bytesRead;
      while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }
    }
  }
}
