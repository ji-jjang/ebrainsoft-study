package com.juny.finalboard.global.security.admin.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("success", true);

    response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
  }
}
