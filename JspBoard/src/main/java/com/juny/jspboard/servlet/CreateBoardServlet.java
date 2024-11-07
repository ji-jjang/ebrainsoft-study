package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CreateBoardServlet implements BoardControllerServlet {

  private final BoardDAO boardDAO = new BoardDAOImpl();

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    List<String> categories = boardDAO.getCategories();
    req.setAttribute("categories", categories);

    req.getRequestDispatcher("/createBoard.jsp").forward(req, res);
  }
}
