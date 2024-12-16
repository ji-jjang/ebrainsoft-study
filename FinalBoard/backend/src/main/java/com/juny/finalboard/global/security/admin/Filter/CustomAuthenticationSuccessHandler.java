package com.juny.finalboard.global.security.admin.Filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  /**
   *
   *
   * <h1>인증 성공 핸들러 </h1>
   *
   * <br>
   * - 인증 성공 시 루트 경로로 리다이렉트
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

    response.sendRedirect("/admin/home");
  }
}
