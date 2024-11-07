package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateCommentServlet implements BoardControllerServlet {

  private final BoardDAO boardDAO = new BoardDAOImpl();

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    Long boardId = extractBoardId(req.getRequestURI());
    String name = req.getParameter("name");
    String password = req.getParameter("password");
    String content = req.getParameter("content");

    boardDAO.createComment(boardId, name, password, content);
    res.sendRedirect("/boards/free/view/" + boardId);
  }

  private Long extractBoardId(String requestURI) {

    String[] split = requestURI.split("/");

    return Long.parseLong(split[2]);
  }
}
