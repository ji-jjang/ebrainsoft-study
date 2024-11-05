package com.juny.jspboard.servlet;

import static org.mockito.Mockito.*;

import com.juny.jspboard.constant.ErrorMessage;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;



class DispatcherServletTest {

  @Test
  @DisplayName("/boards/free/write 접두사로 시작하는 요청은 CreateBoardServlet에게 전달된다.")
  public void dispatcherTest1() throws ServletException, IOException {

    // given
    HttpServletRequest request = mock(HttpServletRequest.class);
    MyDispatcher dispatcher = new MyDispatcher();
    CreateBoardServlet createBoardServletSpy = spy(new CreateBoardServlet());
    dispatcher.getHandlerMappings().put("/boards/free/write", createBoardServletSpy);

    // when
    when(request.getRequestURI()).thenReturn("/boards/free/write");

    // then
    dispatcher.service(request, null);
    verify(createBoardServletSpy, times(1)).execute(request, null);
  }

  @Test
  @DisplayName("처리할 핸들러가 등록되지 않았다면, 에러가 발생한다.")
  public void dispatcherTest2() throws ServletException, IOException {

    String requestURI = "/juny";

    // given
    HttpServletRequest request = mock(HttpServletRequest.class);
    MyDispatcher dispatcher = new MyDispatcher();

    // when
    when(request.getRequestURI()).thenReturn(requestURI);

    // then
    Assertions.assertThatThrownBy(() -> dispatcher.service(request, null))
      .isInstanceOf(RuntimeException.class)
      .hasMessageContaining(ErrorMessage.NO_HANDLER_MSG + request.getRequestURI());
  }
}
