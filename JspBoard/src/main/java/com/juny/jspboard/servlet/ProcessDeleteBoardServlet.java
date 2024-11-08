package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/processDeleteBoard")
public class ProcessDeleteBoardServlet extends HttpServlet implements BoardControllerServlet {

  private final BoardDAO boardDAO = new BoardDAOImpl();
  private final BoardValidator validator = new BoardValidator();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    execute(req, res);
  }

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    // TODO: PasswordCheck

    String password = req.getParameter("password");
    String boardId = req.getParameter("boardId");
    String[] deleteImages = req.getParameterValues("deleteImages");
    String[] deleteAttachments = req.getParameterValues("deleteAttachments");

    // TODO: validator validate params.

    boardDAO.deleteBoard(Long.parseLong(boardId), deleteImages, deleteAttachments);

    String redirectUrl = "/boards/free/list";
    res.sendRedirect(redirectUrl);
  }
}
