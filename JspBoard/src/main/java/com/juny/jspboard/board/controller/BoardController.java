package com.juny.jspboard.board.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface BoardController {

  public void execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException;
}
