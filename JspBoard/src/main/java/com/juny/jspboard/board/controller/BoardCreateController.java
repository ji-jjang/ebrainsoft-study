package com.juny.jspboard.board.controller;

import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.board.dao.CategoryDAO;
import com.juny.jspboard.board.dao.CategoryDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BoardCreateController implements BoardController {

  private final CategoryDAO categoryDAO = new CategoryDAOImpl();

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    List<String> categories = categoryDAO.getCategories();

    req.setAttribute(Constants.CATEGORIES, categories);
    req.getRequestDispatcher("/createBoard.jsp").forward(req, res);
  }
}
