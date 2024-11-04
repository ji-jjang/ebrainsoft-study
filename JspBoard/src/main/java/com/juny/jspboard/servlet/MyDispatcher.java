package com.juny.jspboard.servlet;

import com.juny.jspboard.constant.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyDispatcher {

  private Map<String, BoardControllerServlet> handlerMappings = new HashMap<>();

  public MyDispatcher() {
    this.handlerMappings.put("/boards/free/write", new CreateBoardServlet());
    this.handlerMappings.put("/boards/free/list", new BoardListServlet());
    this.handlerMappings.put("/boards/free/view", new BoardDetailServlet());
    this.handlerMappings.put("/boards/free/modify", new UpdateBoardServlet());
    this.handlerMappings.put("/boards/free/delete", new DeleteBoardServlet());
  }

  public Map<String, BoardControllerServlet> getHandlerMappings() {
    return handlerMappings;
  }

  public void setHandlerMappings(Map<String, BoardControllerServlet> handlerMappings) {
    this.handlerMappings = handlerMappings;
  }

  public void execute(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    boolean isFound = false;
    for (var entry : handlerMappings.entrySet()) {
      var key = entry.getKey();
      if (request.getRequestURI().startsWith(key)) {
        BoardControllerServlet servlet = handlerMappings.get(key);
        servlet.execute(request, response);
        isFound = true;
      }
    }
    if (!isFound) {
      throw new RuntimeException(ErrorMessage.NO_HANDLER_MSG + request.getRequestURI());
    }
  }
}
