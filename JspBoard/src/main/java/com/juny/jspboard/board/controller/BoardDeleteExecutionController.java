package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dto.ReqBoardDelete;
import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BoardDeleteExecutionController implements BoardController {

  private final BoardDAO boardDAO;
  private final BoardValidator validator;

  public BoardDeleteExecutionController(BoardDAO boardDAO, BoardValidator validator) {
    this.boardDAO = boardDAO;
    this.validator = validator;
  }

  private static long getBoardId(HttpServletRequest req) {
    return Long.parseLong(
        req.getRequestURI().substring(req.getRequestURI().lastIndexOf(Constants.SLASH_SIGN) + 1));
  }

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    ReqBoardDelete reqBoardDelete = extreactRequestParams(req);

    validator.validateDeleteExecutionBoard(reqBoardDelete);

    boardDAO.deleteBoard(
      reqBoardDelete.boardId(),
      reqBoardDelete.deleteImages(),
      reqBoardDelete.deleteAttachments(),
      reqBoardDelete.deleteComments());

    String redirectUrl = "/boards/free/list";
    res.sendRedirect(redirectUrl);
  }

  private ReqBoardDelete extreactRequestParams(HttpServletRequest req) {
    long boardId = getBoardId(req);
    String password = req.getParameter(Constants.PASSWORD);
    String method = req.getMethod();
    String[] deleteImages = req.getParameterValues(Constants.DELETE_IMAGES);
    String[] deleteAttachments = req.getParameterValues(Constants.DELETE_ATTACHMENTS);
    String[] comments = req.getParameterValues(Constants.DELETE_COMMENTS);
    List<Long> deleteComments = null;
    if (comments != null && comments.length > 0) {
      deleteComments = Arrays.stream(req.getParameterValues(Constants.DELETE_COMMENTS))
        .map(Long::parseLong).toList();
    }

    return new ReqBoardDelete(boardId, password, method, deleteImages, deleteAttachments, deleteComments);
  }
}
