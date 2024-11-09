package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dto.ReqCommentCreate;
import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentCreateController implements BoardController {

  private final BoardDAO boardDAO;
  private final BoardValidator validator;

  public CommentCreateController(BoardDAO boardDAO, BoardValidator validator) {
    this.boardDAO = boardDAO;
    this.validator = validator;
  }

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException {

    ReqCommentCreate reqCommentCreate = extractReqCreateComment(req);

    validator.validateCreateCommentParams(reqCommentCreate);

    boardDAO.createComment(
        reqCommentCreate.boardId(),
        reqCommentCreate.name(),
        reqCommentCreate.password(),
        reqCommentCreate.content());

    res.sendRedirect("/boards/free/view/" + reqCommentCreate.boardId());
  }

  private ReqCommentCreate extractReqCreateComment(HttpServletRequest req) {

    Long boardId = Long.parseLong(req.getRequestURI().split(Constants.SLASH_SIGN)[2]);
    String name = req.getParameter(Constants.NAME);
    String password = req.getParameter(Constants.PASSWORD);
    String content = req.getParameter(Constants.CONTENT);

    return new ReqCommentCreate(boardId, name, password, content, req.getMethod());
  }
}
