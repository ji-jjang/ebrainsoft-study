package com.juny.finalboard.global.security.admin.Filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  /**
   *
   *
   * <h1>인증 실패 핸들러 </h1>
   *
   * <br>
   * - 401 응답과 에러메시지 반환
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param exception AuthenticationException
   * @throws IOException IOException
   * @throws ServletException ServletException
   */
  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.getWriter().write(exception.getMessage());
  }
}
