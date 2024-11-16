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

@WebServlet("/fileDownloads")
public class FileDownloadServlet extends HttpServlet {

  private BoardValidator validator;

  @Override
  public void init() {
    BoardControllerFactory factory =
        (BoardControllerFactory) getServletContext().getAttribute("boardControllerFactory");

    this.validator = factory.getValidator();
  }

  /**
   *
   *
   * <h1>첨부 파일 처리 </h1>
   *
   * 첨부 파일을 클릭하면 다운로드 받을 수 있도록 파일을 경로에서 읽어 쓰기
   *
   * @param req an {@link HttpServletRequest} object that contains the request the client has made
   *     of the servlet
   * @param res an {@link HttpServletResponse} object that contains the response the servlet sends
   *     to the client
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    validator.validateFileDownloadServlet(req);

    String filePath = req.getParameter(Constants.FILE_PATH);
    String logicalName =
        req.getParameter(Constants.LOGICAL_NAME) + req.getParameter(Constants.EXTENSION);
    String fileName = req.getParameter(Constants.FILE_NAME) + req.getParameter(Constants.EXTENSION);

    File file = new File(filePath, fileName);
    if (!file.exists()) {
      res.sendError(
          HttpServletResponse.SC_NOT_FOUND, ErrorMessage.FILE_NOT_FOUND_MSG + filePath + fileName);
      return;
    }

    res.setContentType(Constants.CONTENT_TYPE_OCTET_STREAM);
    res.setHeader(
        Constants.CONTENT_DISPOSITION, String.format(Constants.ATTACHMENT_FILENAME, logicalName));

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
