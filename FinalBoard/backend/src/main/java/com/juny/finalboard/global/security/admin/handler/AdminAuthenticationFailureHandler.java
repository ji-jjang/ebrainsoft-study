package com.juny.finalboard.global.security.admin.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juny.finalboard.global.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {

  /**
   *
   *
   * <h1>인증 실패 응답 </h1>
   *
   * @param response HttpServletResponse
   * @param exception AuthenticationException
   * @throws IOException IOException
   */
  public static void writeFailedResponse(
      HttpServletResponse response, AuthenticationException exception) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(Constants.APPLICATION_JSON_TYPE);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put(Constants.SUCCESS, false);
    responseBody.put(Constants.MSG, exception.getMessage());

    response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
  }

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

    writeFailedResponse(response, exception);
  }
}
