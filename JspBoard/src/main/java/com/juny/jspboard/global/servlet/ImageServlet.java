package com.juny.jspboard.global.servlet;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.config.Env;
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

  private static File getFile(HttpServletResponse resp, String filename) throws IOException {
    File file = new File(Env.IMAGE_PATH, filename);
    if (!file.exists()) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return null;
    }
    return file;
  }

  /**
   * <h1> 이미지 파일 처리 </h1>
   * 이미지를 읽어 보여줄 수 있도록 경로에서 파일을 읽어 출력
   * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
   *
   * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
   *
   * @throws IOException
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String filename = req.getPathInfo().substring(1);

    File file = getFile(resp, filename);
    if (file == null) return;

    readAndWriteFile(resp, file);
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
