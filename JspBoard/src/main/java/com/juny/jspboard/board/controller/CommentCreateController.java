package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dto.ReqCommentCreate;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentCreateController implements BoardController {

  private final BoardService boardService;

  public CommentCreateController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>댓글 추가 : 추후 CommentService 확장 예정</h1>
   *
   * @param req HTTP 요청
   * @param res
   * @return View
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res) throws IOException {

    ReqCommentCreate reqCommentCreate = extractReqCreateComment(req);

    boardService.createComment(reqCommentCreate);

    return Constants.REDIRECT_PREFIX + "/boards/free/view/" + reqCommentCreate.boardId();
  }

  private ReqCommentCreate extractReqCreateComment(HttpServletRequest req) {

    Long boardId = Long.parseLong(req.getRequestURI().split(Constants.SLASH_SIGN)[2]);
    String name = req.getParameter(Constants.NAME);
    String password = req.getParameter(Constants.PASSWORD);
    String content = req.getParameter(Constants.CONTENT);

    return new ReqCommentCreate(boardId, name, password, content, req.getMethod());
  }
}
