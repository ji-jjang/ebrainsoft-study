package com.juny.finalboard.global.security.user.filter;

import static com.juny.finalboard.global.security.admin.filter.AdminLoginFilter.getAuthentication;

import com.juny.finalboard.global.constant.Constants;
import com.juny.finalboard.global.security.user.handler.UserAuthenticationFailureHandler;
import com.juny.finalboard.global.security.user.handler.UserAuthenticationSuccessHandler;
import com.juny.finalboard.global.security.user.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class UserLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  /**
   *
   *
   * <h1>사용자 로그인 필터 생성자 </h1>
   *
   * <br>
   * - USERNAME_PARAMETER, LOGIN ENDPOINT 재정의
   *
   * @param authenticationManager AuthenticationManager
   */
  public UserLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {

    String USERNAME_PARAMETER = Constants.EMAIL;
    String LOGIN_ENDPOINT = "/api/v1/auth/login";
    String LOGIN_ENDPOINT_METHOD = Constants.HTTP_POST_METHOD;

    this.authenticationManager = authenticationManager;
    this.setAuthenticationSuccessHandler(new UserAuthenticationSuccessHandler(jwtUtil));
    this.setAuthenticationFailureHandler(new UserAuthenticationFailureHandler());

    setUsernameParameter(USERNAME_PARAMETER);
    setRequiresAuthenticationRequestMatcher(
        new AntPathRequestMatcher(LOGIN_ENDPOINT, LOGIN_ENDPOINT_METHOD));
  }

  /**
   *
   *
   * <h1>폼 로그인 방식으로 인증 </h1>
   *
   * @param req HttpServletRequest
   * @return Authentication
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {

    return getAuthentication(req, authenticationManager);
  }

  /**
   *
   *
   * <h1>인증 성공 후 SecurityContext 인증 정보 저장</h1>
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param chain FilterChain
   * @param authResult Authentication
   * @throws IOException IOException
   * @throws ServletException ServletException
   */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {

    SecurityContextHolder.getContext().setAuthentication(authResult);

    getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
  }

  /**
   *
   *
   * <h1>인증 실패 후 실패 핸들러 호출 </h1>
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param failed AuthenticationException
   * @throws IOException IOException
   * @throws ServletException ServletException
   */
  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {

    super.unsuccessfulAuthentication(request, response, failed);
  }
}
