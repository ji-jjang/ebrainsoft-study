package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dto.ReqBoardDetail;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.board.dto.ResBoardDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardDetailController implements BoardController {

  private final BoardService boardService;

  public BoardDetailController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>게시판 상세 조회 </h1>
   *
   * @param req HTTP 요청
   * @param res
   * @return View
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    ReqBoardDetail reqBoardDetail = extractReqBoardDetail(req);

    ResBoardDetail board = boardService.getBoardDetail(reqBoardDetail);
    req.setAttribute(Constants.BOARD, board);
    return "/board.jsp";
  }

  private ReqBoardDetail extractReqBoardDetail(HttpServletRequest req) {

    String suffix =
        req.getRequestURI().substring(req.getRequestURI().lastIndexOf(Constants.SLASH_SIGN) + 1);
    long boardId = Long.parseLong(suffix.substring(0));
    String method = req.getMethod();

    return new ReqBoardDetail(boardId, method);
  }
}
