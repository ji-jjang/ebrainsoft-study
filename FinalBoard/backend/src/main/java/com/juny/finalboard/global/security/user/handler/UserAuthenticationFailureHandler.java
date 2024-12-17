package com.juny.finalboard.global.security.user.handler;

import com.juny.finalboard.global.security.admin.handler.AdminAuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

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
   */
  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {

    AdminAuthenticationFailureHandler.writeFailedResponse(response, exception);
  }
}
