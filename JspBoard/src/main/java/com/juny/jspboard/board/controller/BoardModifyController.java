package com.juny.jspboard.board.controller;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dto.ResBoardDetail;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardModifyController implements BoardController {

  private final BoardDAO boardDAO;
  private final BoardValidator validator;

  public BoardModifyController(BoardDAO boardDAO, BoardValidator validator) {
    this.boardDAO = boardDAO;
    this.validator = validator;
  }

  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    validator.validateModifyBoardServlet(req);

    Long boardId = extractBoardId(req.getRequestURI());

    ResBoardDetail board = boardDAO.getBoardDetail(boardId);

    req.setAttribute(Constants.BOARD, board);
    return "/modifyBoard.jsp";
  }

  private Long extractBoardId(String requestURI) {

    return Long.parseLong(requestURI.split(Constants.SLASH_SIGN)[4]);
  }
}
