package com.juny.jspboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateBoardServlet implements BoardControllerServlet {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    System.out.println("UpdateBoardServlet.execute");
  }
}
