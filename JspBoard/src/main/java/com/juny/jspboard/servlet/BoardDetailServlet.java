package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import com.juny.jspboard.dto.ResBoardDetail;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardDetailServlet implements BoardControllerServlet {

  private BoardDAO boardDAO = new BoardDAOImpl();
  private BoardValidator validator = new BoardValidator();

  // TODO 상세 페이지를 들어가면 view_count가 증가하나, 다시 목록에 갔을 때는 갱신되지 않음. 추후 비동기 로직 추가할 필요 있음
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    Long boardId = validator.validateBoardDetail(req);

    boardDAO.increaseViewCount(boardId);
    ResBoardDetail board = boardDAO.getBoardDetail(boardId);

    req.setAttribute("board", board);
    req.getRequestDispatcher("/board.jsp").forward(req, res);
  }
}
