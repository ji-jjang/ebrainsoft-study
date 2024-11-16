package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dto.ReqBoardList;
import com.juny.jspboard.board.dto.ResBoardList;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class BoardListController implements BoardController {

  private final BoardService boardService;

  public BoardListController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>게시판 목록 페이지 </h1>
   *
   * <br>
   * - 1. 파라미터 검증 <br>
   * - 2. 검색 조건이 있을때와 없을 때 게시글 목록 조회 화면 출력 <br>
   * - categories 이름 받아오는 쿼리 + 검색 조건 처리하는 쿼리 + 페이징 처리 <br>
   * - 페이징 이동하면서 검색 조건 기억하기 위해 쿼리스트링 저장
   *
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException, SQLException {

    ReqBoardList reqBoardList =
        new ReqBoardList(
            req.getParameter(Constants.START_DATE),
            req.getParameter(Constants.END_DATE),
            req.getParameter(Constants.CATEGORY),
            req.getParameter(Constants.KEYWORD),
            getPageNumber(req),
            req.getMethod());

    ResBoardList resBoardList = boardService.getBoardList(reqBoardList);

    req.setAttribute(Constants.DEFAULT_START_DATE, resBoardList.defaultStartDate());
    req.setAttribute(Constants.DEFAULT_END_DATE, resBoardList.defaultEndDate());
    req.setAttribute(Constants.CATEGORIES, resBoardList.categories());
    req.setAttribute(Constants.TOTALS, resBoardList.totals());
    req.setAttribute(Constants.PAGE, reqBoardList.page());
    req.setAttribute(Constants.TOTAL_PAGES, resBoardList.totalPages());
    req.setAttribute(Constants.BOARDS, resBoardList.boards());
    req.setAttribute(Constants.QUERY_PARAMS, extractQueryParams(resBoardList.searchConditions()));

    return "/boards.jsp";
  }

  private int getPageNumber(HttpServletRequest req) {
    String pageParameter = req.getParameter(Constants.PAGE);
    int page = (pageParameter == null ? 1 : Integer.parseInt(pageParameter));
    return page;
  }

  private String extractQueryParams(Map<String, String> searchConditions) {

    StringBuilder sb = new StringBuilder();

    for (var entry : searchConditions.entrySet()) {
      sb.append(Constants.AMPERSAND_SIGN)
          .append(entry.getKey())
          .append(Constants.EQUALS_SIGN)
          .append(entry.getValue());
    }
    return sb.toString();
  }
}
