package com.juny.finalboard.global.security.admin.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class AdminLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public AdminLoginFilter(AuthenticationManager authenticationManager) {

    String USERNAME_PARAMETER = "email";
    String LOGIN_ENDPOINT = "/admin/login";
    String LOGIN_ENDPOINT_METHOD = "POST";

    this.authenticationManager = authenticationManager;
    this.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
    this.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

    setUsernameParameter(USERNAME_PARAMETER);
    setRequiresAuthenticationRequestMatcher(
        new AntPathRequestMatcher(LOGIN_ENDPOINT, LOGIN_ENDPOINT_METHOD));
  }

  /**
   *
   *
   * <h1>로그인 필터에서 로그인 시도</h1>
   *
   * <br>
   * - 이메일과 패스워드로 usernamePasswordAuthenticationToken 만든 후 인증 관리자에게 전달
   *
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @return Authentication
   * @throws AuthenticationException AuthenticationException
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {

    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (email == null || email.isEmpty()) {

      throw new AuthenticationServiceException("email is null or empty");
    }

    if (password == null || password.isEmpty()) {

      throw new AuthenticationServiceException("password is null or empty");
    }

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(email, password, null);

    return authenticationManager.authenticate(authToken);
  }

  /**
   *
   *
   * <h1>인증 성공 후 SecurityContext 및 세션에 인증 정보 저장</h1>
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

    HttpSession session = request.getSession(false);
    if (session != null) {
      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          SecurityContextHolder.getContext());
    }

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

    getFailureHandler().onAuthenticationFailure(request, response, failed);
  }
}
