package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.service.BoardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardDeleteController implements BoardController {

  private final BoardService boardService;

  public BoardDeleteController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>게시판 삭제 전처리</h1>
   *
   * <br>
   * - JSP 상세 페이지에서 첨부 파일과 이미지 파일 정보를 가져옴 <br>
   * - 초기 구현에는 파일 삭제할 때 파일 경로를 전달해주면 검색 쿼리를 아낄 수 있다고 생각했음 <br>
   * - 하지만, 악의적인 클라이언트가 다른 파일 삭제할 가능성 있음. 따라서 Path 넘기는 방식 지양
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

    boardService.preProcessDelete(req);

    return "/deleteBoardPasswordCheck.jsp";
  }
}
