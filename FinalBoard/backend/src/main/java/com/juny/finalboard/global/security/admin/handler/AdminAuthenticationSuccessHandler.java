package com.juny.finalboard.global.security.admin.handler;

import com.juny.finalboard.global.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  /**
   *
   *
   * <h1>인증 성공 핸들러 </h1>
   *
   * <br>
   * - 인증 성공 시 성공 상태 및 메시지 반환
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param authentication Authentication
   * @throws IOException IoException
   */
  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    response.sendRedirect(Constants.ADMIN_LOGIN_SUCCESS_REDIRECT_URL);
  }
}
