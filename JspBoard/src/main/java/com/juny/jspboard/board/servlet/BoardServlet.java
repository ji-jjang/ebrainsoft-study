package com.juny.jspboard.board.servlet;

import com.juny.jspboard.board.controller.BoardCreateController;
import com.juny.jspboard.constant.ErrorMessage;
import com.juny.jspboard.board.controller.BoardController;
import com.juny.jspboard.board.controller.BoardDetailController;
import com.juny.jspboard.board.controller.BoardListController;
import com.juny.jspboard.board.controller.CommentCreateController;
import com.juny.jspboard.board.controller.BoardDeleteController;
import com.juny.jspboard.board.controller.BoardModifyController;
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

/** 커맨드 패턴을 적용한 Board Servlet, Http 요청을 보고 알맞은 컨트롤러에게 전달 */
@WebServlet(urlPatterns = "/boards/*")
public class BoardServlet extends HttpServlet {

  private Map<String, BoardController> exactMappings = new HashMap<>();
  private Map<Pattern, BoardController> regexMappings = new HashMap<>();

  /**
   * URL에 따라 처리할 핸들러 등록 extractMapping는 정확한 url을 가진 Controller 검색, regexMapping는 정규식을 통해 해당하는
   * Controller 검색
   */
  public BoardServlet() {
    this.exactMappings.put("/boards/free/write", new BoardCreateController());
    this.exactMappings.put("/boards/free/list", new BoardListController());
    this.regexMappings.put(
        Pattern.compile("^/boards/free/view/[0-9]+"), new BoardDetailController());
    this.exactMappings.put("/boards/free/delete", new BoardDeleteController());
    this.regexMappings.put(
        Pattern.compile("^/boards/[0-9]+/comments$"), new CommentCreateController());
    this.regexMappings.put(
        Pattern.compile("^/boards/free/modify/[0-9]+"), new BoardModifyController());
  }

  /**
   * Gets handler mappings.
   *
   * @return the handler mappings
   */
  public Map<String, BoardController> getExactMappings() {
    return exactMappings;
  }

  /**
   * Sets handler mappings.
   *
   * @param exactMappings the handler mappings
   */
  public void setExactMappings(Map<String, BoardController> exactMappings) {
    this.exactMappings = exactMappings;
  }

  /**
   * Service 함수에서 HTTP 요청을 처리할 컨트롤러를 찾아 실행
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
        BoardController servlet = exactMappings.get(key);
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
