package com.juny.jspboard.board.servlet;

import com.juny.jspboard.board.servlet.support.BoardControllerFactory;
import com.juny.jspboard.board.servlet.support.BoardControllerResolver;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardDAOImpl;
import com.juny.jspboard.board.dao.CategoryDAO;
import com.juny.jspboard.board.dao.CategoryDAOImpl;
import com.juny.jspboard.board.controller.BoardController;
import com.juny.jspboard.constant.ErrorMessage;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/boards/*")
public class BoardDispatcherServlet extends HttpServlet {

  private BoardControllerResolver controllerResolver;

  /**
   * /boards 로 시작하는 모든 요청을 처리하는 서블릿 컨트롤러가 사용하는 구현체 직접 생성하여 BoardControllerFactory 전달
   * ControllerResolver 로부터 실행할 컨트롤러를 넘겨받아 실행하는 역할
   *
   * <p>필드 주입에서 생성자 주입 방식으로 변경하고, 컨트롤러 인스턴스 생성을 Factory 객체에게 위임 요청 URL 해당하는 컨트롤러를 찾는 로직은
   * ControllerResolver 객체에게 위임
   *
   * @throws ServletException
   */
  @Override
  public void init() {

    BoardDAO boardDAO = new BoardDAOImpl();
    CategoryDAO categoryDAO = new CategoryDAOImpl();
    BoardValidator validator = new BoardValidator();

    BoardControllerFactory factory = new BoardControllerFactory(categoryDAO, boardDAO, validator);
    this.controllerResolver = new BoardControllerResolver(factory);

    getServletContext().setAttribute("boardControllerFactory", factory);
  }

  /**
   * Service 함수에서 execute() 함수로 컨트롤러 실행 커맨드 패턴 적용된 컨트롤러는 url 해당하는 역할 수행
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
    controller.execute(request, response);
  }
}
