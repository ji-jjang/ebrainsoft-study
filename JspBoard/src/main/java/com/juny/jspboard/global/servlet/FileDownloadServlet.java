package com.juny.jspboard.global.servlet;

import com.juny.jspboard.board.servlet.BoardControllerFactory;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/** 첨부파일 리스트를 클릭하면 FileDownloadServlet 실행 */
@WebServlet("/fileDownloads")
public class FileDownloadServlet extends HttpServlet {

  private BoardValidator validator;

  @Override
  public void init() throws ServletException {
    BoardControllerFactory factory =
      (BoardControllerFactory) getServletContext().getAttribute("boardControllerFactory");

    this.validator = factory.createBoardValidator();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    validator.validateFileDownloadServlet(req);

    String filePath = req.getParameter(Constants.FILE_PATH);
    String fileName = req.getParameter(Constants.FILE_NAME) + req.getParameter(Constants.EXTENSION);

    File file = new File(filePath, fileName);
    if (!file.exists()) {
      res.sendError(
          HttpServletResponse.SC_NOT_FOUND, ErrorMessage.FILE_NOT_FOUND_MSG + filePath + fileName);
      return;
    }

    res.setContentType(Constants.CONTENT_TYPE_OCTET_STREAM);
    res.setHeader(
        Constants.CONTENT_DISPOSITION, String.format(Constants.ATTACHMENT_FILENAME, fileName));

    try (FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = res.getOutputStream()) {

      byte[] buffer = new byte[Constants.FILE_READ_BUFFER_SIZE];
      int bytesRead;
      while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      throw new ServletException(ErrorMessage.FILE_DOWNLOAD_FAIL_MSG, e);
    }
  }
}
