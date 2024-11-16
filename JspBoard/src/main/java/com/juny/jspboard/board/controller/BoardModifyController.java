package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dto.ReqBoardUpdatePre;
import com.juny.jspboard.board.dto.ResBoardDetail;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class BoardModifyController implements BoardController {

  private final BoardService boardService;

  public BoardModifyController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>게시판 수정 전처리: 수정하기 전 게시판 정보 가져오기 </h1>
   *
   * @param req HTTP 요청
   * @param res
   * @return View
   * @throws ServletException
   * @throws IOException
   * @throws SQLException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException, SQLException {

    ReqBoardUpdatePre reqBoardUpdate = extractBoardPreUpdate(req);

    ResBoardDetail board = boardService.preProcessModify(reqBoardUpdate);

    req.setAttribute(Constants.BOARD, board);
    return "/modifyBoard.jsp";
  }

  private ReqBoardUpdatePre extractBoardPreUpdate(HttpServletRequest req) {
    long boardId = Long.parseLong(req.getRequestURI().split(Constants.SLASH_SIGN)[4]);
    String method = req.getMethod();
    return new ReqBoardUpdatePre(boardId, method);
  }
}
