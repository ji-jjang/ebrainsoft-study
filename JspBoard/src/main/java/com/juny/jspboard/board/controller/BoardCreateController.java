package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dto.ResCategoryName;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BoardCreateController implements BoardController {

  private final BoardService boardService;

  public BoardCreateController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>게시판 생성 전처리</h1>
   *
   * - 카테고리 목록을 게시판 생성 폼에 추가하는 작업
   *
   * @param req
   * @param res
   * @return View
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    List<ResCategoryName> categories = boardService.getCategories();

    req.setAttribute(Constants.CATEGORIES, categories);
    return "/createBoard.jsp";
  }
}
