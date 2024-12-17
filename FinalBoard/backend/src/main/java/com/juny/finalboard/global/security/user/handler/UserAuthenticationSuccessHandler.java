package com.juny.finalboard.global.security.user.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juny.finalboard.global.constant.Constants;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import com.juny.finalboard.global.security.user.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;

  public UserAuthenticationSuccessHandler(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  /**
   *
   *
   * <h1>인증 성공 핸들러 </h1>
   *
   * <br>
   * - 유저 인증 성공 시 액세스 토큰 및 만료기간 반환<br>
   * - 추후 리프레시 토큰 반환 가능
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

    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    Date expiredDate =
        new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenExpiredMileSecond());

    String accessToken =
        jwtUtil.createJwt(customUserDetails.getId(), customUserDetails.getRole(), expiredDate);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(Constants.APPLICATION_JSON_TYPE);
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put(Constants.SUCCESS, true);
    responseBody.put(Constants.ACCESS_TOKEN, accessToken);
    responseBody.put(
        Constants.ACCESS_TOKEN_EXPIRED,
        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(jwtUtil.getExpiration(accessToken)));

    response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
  }
}
