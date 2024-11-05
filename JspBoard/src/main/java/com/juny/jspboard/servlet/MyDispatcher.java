package com.juny.jspboard.servlet;

import com.juny.jspboard.constant.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 커맨드 패턴을 적용한 Dispatcher, Http 요청 처리
 */
@WebServlet(urlPatterns = "/boards/*")
public class MyDispatcher extends HttpServlet {

  private Map<String, BoardControllerServlet> handlerMappings = new HashMap<>();

  /**
   * URL에 따라 처리할 핸들러 등록
   * TODO 해당 서블릿은 실행되면서, 제일 먼저 URL 검증
   */
  public MyDispatcher() {
    this.handlerMappings.put("/boards/free/write", new CreateBoardServlet());
    this.handlerMappings.put("/boards/free/list", new BoardListServlet());
    this.handlerMappings.put("/boards/free/view", new BoardDetailServlet());
    this.handlerMappings.put("/boards/free/modify", new UpdateBoardServlet());
    this.handlerMappings.put("/boards/free/delete", new DeleteBoardServlet());
  }

  /**
   * Gets handler mappings.
   *
   * @return the handler mappings
   */
  public Map<String, BoardControllerServlet> getHandlerMappings() {
    return handlerMappings;
  }

  /**
   * Sets handler mappings.
   *
   * @param handlerMappings the handler mappings
   */
  public void setHandlerMappings(Map<String, BoardControllerServlet> handlerMappings) {
    this.handlerMappings = handlerMappings;
  }

  /**
   * Service 함수에서 HTTP 요청을 처리할 서블릿을 찾아 실행
   *
   * @param request : http 요청
   * @param response : http 응답
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    boolean isFound = false;
    for (var entry : handlerMappings.entrySet()) {
      var key = entry.getKey();
      if (request.getRequestURI().startsWith(key)) {
        BoardControllerServlet servlet = handlerMappings.get(key);
        servlet.execute(request, response);
        isFound = true;
        break;
      }
    }
    if (!isFound) {
      throw new RuntimeException(ErrorMessage.NO_HANDLER_MSG + request.getRequestURI());
    }
  }
}
