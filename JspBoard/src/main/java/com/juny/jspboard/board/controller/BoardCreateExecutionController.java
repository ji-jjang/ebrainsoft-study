package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.board.dto.ReqBoardCreate;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardCreateExecutionController implements BoardController {

  private final BoardService boardService;

  public BoardCreateExecutionController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>게시판 생성 후처리</h1>
   *
   * - DB 반영
   *
   * @param req
   * @param res
   * @return View
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    ReqBoardCreate reqBoardCreate = extractReqBoardCreate(req);

    Long boardId = boardService.createBoard(reqBoardCreate, req.getParts());

    return Constants.REDIRECT_PREFIX + "/boards/free/view/" + boardId;
  }

  private ReqBoardCreate extractReqBoardCreate(HttpServletRequest req) {
    String category = req.getParameter(Constants.CATEGORY);
    String createdBy = req.getParameter(Constants.CREATED_BY);
    String password = req.getParameter(Constants.PASSWORD);
    String passwordConfirm = req.getParameter(Constants.PASSWORD_CONFIRM);
    String title = req.getParameter(Constants.TITLE);
    String content = req.getParameter(Constants.CONTENT);
    String method = req.getMethod();

    return new ReqBoardCreate(
        category, createdBy, password, passwordConfirm, title, content, method);
  }
}
