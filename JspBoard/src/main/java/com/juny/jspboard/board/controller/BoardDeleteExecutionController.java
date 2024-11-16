package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dto.ReqBoardDelete;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardDeleteExecutionController implements BoardController {

  private final BoardService boardService;

  public BoardDeleteExecutionController(BoardService boardService) {
    this.boardService = boardService;
  }

  private static long getBoardId(HttpServletRequest req) {
    return Long.parseLong(
        req.getRequestURI().substring(req.getRequestURI().lastIndexOf(Constants.SLASH_SIGN) + 1));
  }

  /**
   *
   *
   * <h1>게시판 삭제 후처리</h1>
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

    ReqBoardDelete reqBoardDelete = extreactRequestParams(req);

    boardService.deleteBoard(reqBoardDelete);

    return Constants.REDIRECT_PREFIX + "/boards/free/list";
  }

  private ReqBoardDelete extreactRequestParams(HttpServletRequest req) {
    long boardId = getBoardId(req);
    String password = req.getParameter(Constants.PASSWORD);
    String method = req.getMethod();

    return new ReqBoardDelete(boardId, password, method);
  }
}
