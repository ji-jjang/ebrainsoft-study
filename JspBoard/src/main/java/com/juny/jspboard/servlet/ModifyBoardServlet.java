package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import com.juny.jspboard.dto.ResBoardDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ModifyBoardServlet implements BoardControllerServlet {

  private final BoardDAO boardDAO = new BoardDAOImpl();
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    Long boardId = extractBoardId(req.getRequestURI());

    ResBoardDetail board = boardDAO.getBoardDetail(boardId);
    req.setAttribute("board", board);
    req.getRequestDispatcher("/modifyBoard.jsp").forward(req, res);

  }

  private Long extractBoardId(String requestURI) {

    return Long.parseLong(requestURI.split("/")[4]);
  }
}
