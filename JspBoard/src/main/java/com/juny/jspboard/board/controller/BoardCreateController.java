package com.juny.jspboard.board.controller;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.board.dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BoardCreateController implements BoardController {

  private final CategoryDAO categoryDAO;

  public BoardCreateController(CategoryDAO categoryDAO) {
    this.categoryDAO = categoryDAO;
  }

  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    List<String> categories = categoryDAO.getCategories();

    req.setAttribute(Constants.CATEGORIES, categories);
    return "/createBoard.jsp";
  }
}
