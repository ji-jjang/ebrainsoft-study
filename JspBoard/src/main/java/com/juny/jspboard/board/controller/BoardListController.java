package com.juny.jspboard.board.controller;

import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardDAOImpl;
import com.juny.jspboard.board.dao.CategoryDAO;
import com.juny.jspboard.board.dao.CategoryDAOImpl;
import com.juny.jspboard.board.dto.ResBoardViewList;
import com.juny.jspboard.utility.TimeFormatterUtils;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BoardListController implements BoardController {

  private final BoardDAO boardDAO = new BoardDAOImpl();
  private final CategoryDAO categoryDAO = new CategoryDAOImpl();

  private BoardValidator validator = new BoardValidator();

  private static int getPageNumber(HttpServletRequest req) {
    String pageParameter = req.getParameter(Constants.PAGE);
    int page = (pageParameter == null ? 1 : Integer.parseInt(pageParameter));
    return page;
  }

  /**
   * 1. 파라미터 검증 2. 검색 조건이 있을때와 없을 때 게시글 목록 조회 화면 출력 - categories 이름 받아오는 쿼리 - 검색 조건 처리하는 쿼리 - 페이징 처리
   * - 페이징 이동하면서 검색 조건 기억하기 위해 쿼리스트링 저장
   *
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    validator.validateBoardListParams(req);

    List<String> categories = categoryDAO.getCategories();
    Map<String, String> searchConditions = getSearchConditions(req);
    int page = getPageNumber(req);
    int totals = getTotalBoards(searchConditions);
    int totalPages = calculateTotalPages(totals);

    List<ResBoardViewList> boards = getBoards(page, searchConditions);
    SearchDate defaultDate = getSearchDate();
    String queryParams = extractQueryParams(searchConditions);

    req.setAttribute(Constants.DEFAULT_START_DATE, defaultDate.defaultStartDate);
    req.setAttribute(Constants.DEFAULT_END_DATE, defaultDate.defaultEndDate);
    req.setAttribute(Constants.CATEGORIES, categories);
    req.setAttribute(Constants.TOTALS, totals);
    req.setAttribute(Constants.PAGE, page);
    req.setAttribute(Constants.TOTAL_PAGES, totalPages);
    req.setAttribute(Constants.BOARDS, boards);
    req.setAttribute(Constants.QUERY_PARAMS, queryParams);
    req.getRequestDispatcher("/boards.jsp").forward(req, res);
  }

  private Map<String, String> getSearchConditions(HttpServletRequest req) {

    Map<String, String> searchConditions = new LinkedHashMap<>();

    String startDate = req.getParameter(Constants.START_DATE);
    String endDate = req.getParameter(Constants.END_DATE);
    String category = req.getParameter(Constants.CATEGORY);
    String keyword = req.getParameter(Constants.KEYWORD);

    if (startDate != null && !startDate.isEmpty()) {
      searchConditions.put(Constants.START_DATE, startDate);
    }
    if (endDate != null && !endDate.isEmpty()) {
      searchConditions.put(Constants.END_DATE, endDate);
    }
    if (category != null && !category.isEmpty()) {
      searchConditions.put(Constants.CATEGORY, category);
    }
    if (keyword != null && !keyword.isEmpty()) {
      searchConditions.put(Constants.KEYWORD, keyword);
    }

    return searchConditions;
  }

  private int getTotalBoards(Map<String, String> searchConditions) {
    if (searchConditions.size() > 0) {
      return boardDAO.getTotalsWithSearchConditions(searchConditions);
    }
    return boardDAO.getTotals();
  }

  private List<ResBoardViewList> getBoards(int page, Map<String, String> searchConditions) {
    if (searchConditions.size() > 0) {
      return boardDAO.getBoardSearchList(page, searchConditions);
    }
    return boardDAO.getBoardList(page);
  }

  private int calculateTotalPages(int totals) {
    return (int) Math.ceil((double) totals / Constants.BOARD_LIST_PAGE_SIZE);
  }

  private SearchDate getSearchDate() {
    String defaultStartDate = TimeFormatterUtils.getDefaultStartDate();
    String defaultEndDate = TimeFormatterUtils.getDefaultEndDate();
    SearchDate defaultDate = new SearchDate(defaultStartDate, defaultEndDate);
    return defaultDate;
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

  private record SearchDate(String defaultStartDate, String defaultEndDate) {}
}
