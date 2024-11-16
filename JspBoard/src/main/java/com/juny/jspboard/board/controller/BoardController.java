package com.juny.jspboard.board.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public interface BoardController {

  /**
   *
   *
   * <h1>커맨드 패턴을 적용한 게시판 인터페이스 </h1>
   *
   * @param req HTTP 요청
   * @param res
   * @return jsp 에서 사용할 View name 반환
   * @throws ServletException
   * @throws IOException
   */
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException, SQLException;
}
