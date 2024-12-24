package com.juny.finalboard.global.security.admin.filter;

import com.juny.finalboard.global.constant.Constants;
import com.juny.finalboard.global.security.admin.handler.AdminAuthenticationFailureHandler;
import com.juny.finalboard.global.security.admin.handler.AdminAuthenticationSuccessHandler;
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

  /**
   *
   *
   * <h1>관리자 로그인 필터 생성자 </h1>
   *
   * <br>
   * - USERNAME_PARAMETER, LOGIN ENDPOINT 재정의
   *
   * @param authenticationManager AuthenticationManager
   */
  public AdminLoginFilter(AuthenticationManager authenticationManager) {

    String USERNAME_PARAMETER = Constants.EMAIL;
    String LOGIN_ENDPOINT = "/admin/v1/login";
    String LOGIN_ENDPOINT_METHOD = Constants.HTTP_POST_METHOD;

    this.authenticationManager = authenticationManager;
    this.setAuthenticationSuccessHandler(new AdminAuthenticationSuccessHandler());
    this.setAuthenticationFailureHandler(new AdminAuthenticationFailureHandler());

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
   * @param authenticationManager AuthenticationManager
   * @return Authentication
   */
  public static Authentication getAuthentication(
      HttpServletRequest req, AuthenticationManager authenticationManager) {
    String email = req.getParameter(Constants.EMAIL);
    String password = req.getParameter(Constants.PASSWORD);

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

    return getAuthentication(req, authenticationManager);
  }

  /**
   *
   *
   * <h1>인증 성공 후 SecurityContext 및 세션에 인증 정보 저장</h1>
   *
   * <br>
   * - 세션 정책이 IF_REQUIRED, CSRF 비활성화 되어 있으면 로그인 성공 후에도 새로운 세션이 만들어지지 않음.<br>
   * - CSRF 비활성화했다면, 세션을 강제로 만들어주어야 Redirect 된 이후 기존 세션에서 인증 정보를 가져올 수 있음.<br>
   * - 추후 CSRF 활성화 후 새로운 세션 만들지 않도록 할 것
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

    HttpSession session = request.getSession(true);
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
