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

public class BoardDetailController implements BoardController {

  private BoardDAO boardDAO = new BoardDAOImpl();
  private BoardValidator validator = new BoardValidator();

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    validator.validateBoardDetailParams(req);

    Long boardId = extractBoardId(req.getRequestURI());

    ResBoardDetail board = boardDAO.getBoardDetail(boardId);

    req.setAttribute(Constants.BOARD, board);
    req.getRequestDispatcher("/board.jsp").forward(req, res);
  }

  private Long extractBoardId(String requestURI) {

    String suffix = requestURI.substring(requestURI.lastIndexOf(Constants.SLASH_SIGN) + 1);
    String seq = suffix.substring(0);

    return Long.parseLong(seq);
  }
}
