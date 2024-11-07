package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import com.juny.jspboard.dto.ResBoardViewList;
import com.juny.jspboard.utility.TimeFormatterUtils;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BoardListServlet implements BoardControllerServlet {

  private BoardDAO boardDAO = new BoardDAOImpl();
  private BoardValidator validator = new BoardValidator();

  /**
   * 1. 파라미터 검증
   * 2. 검색 조건이 있을때와 없을 때 게시글 목록 조회 화면 출력
   *  - categories 이름 받아오는 쿼리
   *  - 검색 조건 처리하는 쿼리
   *  - 페이징 처리
   *  - 페이징 이동하면서 검색 조건 기억하기 위해 쿼리스트링 저장
   *
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    validator.validateBoardList(req);

    String pageParameter = req.getParameter("page");
    int page = (pageParameter == null ? 1 : Integer.parseInt(pageParameter));

    List<String> categories = boardDAO.getCategories();
    req.setAttribute("categories", categories);

    List<ResBoardViewList> boards = null;
    Map<String, String> searchConditions = new LinkedHashMap<>();

    int totals = 0;
    if (hasSearchCondition(req, searchConditions)) {
      boards = boardDAO.getBoardSearchList(page, searchConditions);
      totals = boardDAO.getTotalsWithSearchConditions(searchConditions);
      req.setAttribute("searchConditions", searchConditions);
    } else {
      totals = boardDAO.getTotals();
      boards = boardDAO.getBoardList(page);
    }

    int pageSize = 10;
    int totalPages = (int) Math.ceil((double) totals / pageSize);

    StringBuilder queryParams = new StringBuilder();
    for (var entry : searchConditions.entrySet()) {
      queryParams.append("&").append(entry.getKey()).append("=").append(entry.getValue());
    }

    String defaultStartDate = TimeFormatterUtils.getDefaultStartDate();
    String defaultEndDate = TimeFormatterUtils.getDefaultEndDate();

    req.setAttribute("totals", totals);
    req.setAttribute("defaultStartDate", defaultStartDate);
    req.setAttribute("defaultEndDate", defaultEndDate);
    req.setAttribute("page", page);
    req.setAttribute("totalPages", totalPages);
    req.setAttribute("boards", boards);
    req.setAttribute("queryParams", queryParams.toString());
    req.getRequestDispatcher("/boards.jsp").forward(req, res);
  }

  private boolean hasSearchCondition(HttpServletRequest req, Map<String, String> searchConditions) {

    String startDate = req.getParameter("startDate");
    String endDate = req.getParameter("endDate");
    String category = req.getParameter("category");
    String keyword = req.getParameter("keyword");

    if (startDate != null && !startDate.isEmpty()) {
      searchConditions.put("startDate", startDate);
    }
    if (endDate != null && !endDate.isEmpty()) {
      searchConditions.put("endDate", endDate);
    }
    if (category != null && !category.isEmpty()) {
      searchConditions.put("category", category);
    }
    if (keyword != null && !keyword.isEmpty()) {
      searchConditions.put("keyword", keyword);
    }

    return searchConditions.size() > 0;
  }
}
