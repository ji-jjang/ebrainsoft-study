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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 커맨드 패턴을 적용한 Dispatcher, Http 요청 처리
 */
@WebServlet(urlPatterns = "/boards/*")
public class MyDispatcher extends HttpServlet {

  private Map<String, BoardControllerServlet> exactMappings = new HashMap<>();
  private Map<Pattern, BoardControllerServlet> regexMappings = new HashMap<>();

  /**
   * URL에 따라 처리할 핸들러 등록
   * extractMapping는 정확한 url을 가진 servlet 검색, regexMapping는 정규식을 통해 해당하는 servlet 검색
   */
  public MyDispatcher() {
    this.exactMappings.put("/boards/free/write", new CreateBoardServlet());
    this.exactMappings.put("/boards/free/list", new BoardListServlet());
    this.exactMappings.put("/boards/free/delete", new DeleteBoardServlet());
    this.exactMappings.put("/boards/downloads", new FileDownloadServlet());
    this.exactMappings.put("/boards/processCreateBoard", new ProcessCreateBoardServlet());
    this.regexMappings.put(Pattern.compile("^/boards/free/view/[0-9]+"), new BoardDetailServlet());
    this.regexMappings.put(
      Pattern.compile("^/boards/[0-9]+/comments$"), new CreateCommentServlet());
    this.regexMappings.put(
      Pattern.compile("^/boards/free/modify/[0-9]+"), new ModifyBoardServlet());
  }

  /**
   * Gets handler mappings.
   *
   * @return the handler mappings
   */
  public Map<String, BoardControllerServlet> getExactMappings() {
    return exactMappings;
  }

  /**
   * Sets handler mappings.
   *
   * @param exactMappings the handler mappings
   */
  public void setExactMappings(Map<String, BoardControllerServlet> exactMappings) {
    this.exactMappings = exactMappings;
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

    String requestURI = request.getRequestURI();
    boolean isFound = false;

    for (var entry : exactMappings.entrySet()) {
      var key = entry.getKey();
      if (requestURI.equals(key)) {
        BoardControllerServlet servlet = exactMappings.get(key);
        servlet.execute(request, response);
        isFound = true;
        break;
      }
    }
    if (!isFound) {
      for (var entry : regexMappings.entrySet()) {
        Pattern pattern = entry.getKey();
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
          entry.getValue().execute(request, response);
          isFound = true;
          break;
        }
      }
    }
    if (!isFound) {
      throw new RuntimeException(ErrorMessage.NO_HANDLER_MSG + request.getRequestURI());
    }
  }
}
