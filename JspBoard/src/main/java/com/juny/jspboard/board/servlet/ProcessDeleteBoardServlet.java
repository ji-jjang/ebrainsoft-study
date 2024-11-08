package com.juny.jspboard.board.servlet;

import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardDAOImpl;
import com.juny.jspboard.board.dto.ReqBoardDelete;
import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/processDeleteBoard")
public class ProcessDeleteBoardServlet extends HttpServlet {

  private final BoardDAO boardDAO = new BoardDAOImpl();
  private final BoardValidator validator = new BoardValidator();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

    ReqBoardDelete reqBoardDelete = extreactRequestParams(req);

    validator.validateProcessDeleteBoard(reqBoardDelete);

    boardDAO.deleteBoard(
        reqBoardDelete.boardId(),
        reqBoardDelete.deleteImages(),
        reqBoardDelete.deleteAttachments());

    String redirectUrl = "/boards/free/list";
    res.sendRedirect(redirectUrl);
  }

  private ReqBoardDelete extreactRequestParams(HttpServletRequest req) {
    Long boardId = Long.parseLong(req.getParameter(Constants.BOARD_ID));
    String password = req.getParameter(Constants.PASSWORD);
    String method = req.getMethod();
    String[] deleteImages = req.getParameterValues(Constants.DELETE_IMAGES);
    String[] deleteAttachments = req.getParameterValues(Constants.DELETE_ATTACHMENTS);

    return new ReqBoardDelete(boardId, password, method, deleteImages, deleteAttachments);
  }
}
