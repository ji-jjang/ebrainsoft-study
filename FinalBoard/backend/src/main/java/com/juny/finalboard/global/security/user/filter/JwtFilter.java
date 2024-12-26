package com.juny.finalboard.global.security.user.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.global.constant.Constants;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import com.juny.finalboard.global.security.user.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  /**
   *
   *
   * <h1>JwtFilter </h1>
   *
   * <br>
   * - Authorization 헤더에 "Bearer " 접두사가 붙은 AccessToken 검증<br>
   * - "/api/v1/auth/login" 로그인 요청에서는 새로운 토큰 발급 목적이므로 헤더 검증 생략
   *
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @param filterChain FilterChain
   * @throws ServletException ServletException
   * @throws IOException IOException
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String authorizationHeader = req.getHeader(Constants.AUTHORIZATION_HEADER);

    if (authorizationHeader == null
        || !authorizationHeader.startsWith(Constants.BEARER_PREFIX)
        || req.getRequestURI().equals("/api/v1/auth/login")) {

      filterChain.doFilter(req, res);
      return;
    }

    String accessToken = authorizationHeader.split(Constants.SPACE_SIGN)[1];
    if (!validateAccessToken(accessToken, res)) {
      return;
    }

    storeSecurityContextHolder(accessToken);

    filterChain.doFilter(req, res);
  }

  private boolean validateAccessToken(String accessToken, HttpServletResponse res)
      throws IOException {

    int errorType = jwtUtil.isInValid(accessToken);

    if (errorType == 1) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.setContentType(Constants.APPLICATION_JSON_TYPE);

      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put(Constants.SUCCESS, false);
      responseBody.put(Constants.CODE, "AT1");
      responseBody.put(Constants.MSG, "access token expired");

      res.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
      return false;
    }

    if (errorType == 2) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.setContentType("application/json");

      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put(Constants.SUCCESS, false);
      responseBody.put(Constants.CODE, "AT2");
      responseBody.put(Constants.MSG, "access token is invalid");

      res.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
      return false;
    }

    return true;
  }

  private void storeSecurityContextHolder(String accessToken) {

    User user =
        User.builder().id(jwtUtil.getId(accessToken)).role(jwtUtil.getRole(accessToken)).build();

    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    Authentication authToken =
        new UsernamePasswordAuthenticationToken(
            customUserDetails, null, customUserDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authToken);
  }
}
