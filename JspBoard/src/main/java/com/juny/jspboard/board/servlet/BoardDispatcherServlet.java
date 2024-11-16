package com.juny.jspboard.board.servlet;

import com.juny.jspboard.board.controller.BoardController;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet(urlPatterns = "/boards/*")
@MultipartConfig
public class BoardDispatcherServlet extends HttpServlet {

  private BoardControllerResolver controllerResolver;

  /**
   *
   *
   * <h1>서블릿 초기화 : BoardFactory, ControllerResolver 생성 </h1>
   *
   * BoardFactory: 생성자 방식으로 의존성 주입하기 위해 필요한 인스턴스 생성 ControllerResolver: 실행할 컨트롤러를 url, 정규식 매칭을 통해 찾음
   *
   * @throws ServletException
   */
  @Override
  public void init() {
    BoardControllerFactory factory = new BoardControllerFactory();
    this.controllerResolver = new BoardControllerResolver(factory);

    getServletContext().setAttribute("boardControllerFactory", factory);
  }

  /**
   *
   *
   * <h1>커맨드 패턴을 이용하여 Controller.execute()로 특정 기능 수행</h1>
   *
   * Controller를 실행하고, 반환된 View 이름이 :redirect로 실행된다면 리다이렉트, 아니라면 포워딩
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
    BoardController controller = controllerResolver.resolveController(requestURI);

    if (controller == null) {
      throw new RuntimeException(ErrorMessage.NO_HANDLER_MSG + request.getRequestURI());
    }

    String view = null;
    try {
      view = controller.execute(request, response);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    if (!Objects.isNull(view) && view.startsWith(Constants.REDIRECT_PREFIX)) {
      response.sendRedirect(view.substring(Constants.REDIRECT_PREFIX.length()));
      return;
    }
    request.getRequestDispatcher(view).forward(request, response);
  }
}
