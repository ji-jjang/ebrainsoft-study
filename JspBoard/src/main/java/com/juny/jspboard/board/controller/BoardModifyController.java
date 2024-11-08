package com.juny.jspboard.board.controller;

import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardDAOImpl;
import com.juny.jspboard.board.dto.ResBoardDetail;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardModifyController implements BoardController {

  private final BoardDAO boardDAO = new BoardDAOImpl();
  private final BoardValidator validator = new BoardValidator();

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    validator.validateModifyBoardServlet(req);

    Long boardId = extractBoardId(req.getRequestURI());

    ResBoardDetail board = boardDAO.getBoardDetail(boardId);

    req.setAttribute(Constants.BOARD, board);
    req.getRequestDispatcher("/modifyBoard.jsp").forward(req, res);
  }

  private Long extractBoardId(String requestURI) {

    return Long.parseLong(requestURI.split(Constants.SLASH_SIGN)[4]);
  }
}
