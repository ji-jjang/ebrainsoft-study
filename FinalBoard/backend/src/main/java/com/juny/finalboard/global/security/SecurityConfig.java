package com.juny.finalboard.global.security;

import com.juny.finalboard.global.security.admin.filter.AdminLoginFilter;
import com.juny.finalboard.global.security.user.filter.JwtFilter;
import com.juny.finalboard.global.security.user.filter.UserLoginFilter;
import com.juny.finalboard.global.security.user.jwt.JwtUtil;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUtil jwtUtil;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {

    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {

    return configuration.getAuthenticationManager();
  }

  /**
   *
   *
   * <h1>어드민 Security Filter Chain </h1>
   *
   * <br>
   * - 세션 로그인 방식<br>
   * - 배포 시에 CSRF 토큰 활성화할 것
   *
   * @param http HttpSecurity
   * @param authenticationConfiguration AuthenticationConfiguration
   * @return SecurityFilterChain
   * @throws Exception SecurityMatcher, formLogin, AuthenticationManager Exception
   */
  @Bean
  public SecurityFilterChain adminFilterChain(
      HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {

    http.securityMatcher("/admin/**", "/")
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.GET, "/","/admin/login", "/admin/management")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/admin/v1/login", "/admin/v1/logout")
                    .permitAll()
                    .anyRequest()
                    .hasAuthority("ADMIN"));

    http.logout(
        (auth) ->
            auth.logoutRequestMatcher(req -> "/admin/v1/logout".equals(req.getRequestURI()))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());

    http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable);

    http.addFilterAt(
        new AdminLoginFilter(authenticationManager(authenticationConfiguration)),
        UsernamePasswordAuthenticationFilter.class);

    http
      .csrf(AbstractHttpConfigurer::disable)
      .securityContext(
            (auth) -> auth.securityContextRepository(new HttpSessionSecurityContextRepository()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

    return http.build();
  }

  /**
   *
   *
   * <h1>유저 Security Filter Chain</h1>
   *
   * <br>
   * - 로그인 성공 시 Access Token 발급<br>
   * - 추후 Refresh Token 및 Refresh refreshRotate, blacklisting 전략 사용 가능
   *
   * @param http HttpSecurity
   * @param authenticationConfiguration AuthenticationConfiguration
   * @return SecurityFilterChain
   * @throws Exception SecurityMatcher, formLogin, AuthenticationManager Exception
   */
  @Bean
  public SecurityFilterChain userFilterChain(
      HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {

    http.cors(
        (cors) ->
            cors.configurationSource(
                req -> {
                  CorsConfiguration corsConfiguration = new CorsConfiguration();

                  corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                  corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
                  corsConfiguration.setAllowedMethods(Collections.singletonList("*"));

                  return corsConfiguration;
                }));

    http.securityMatcher("/api/**")
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/announcement-categories",
                        "/api/v1/announcement-posts",
                        "/api/v1/announcement-posts/{postId}")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/login")
                    .permitAll()
                    .anyRequest()
                    .hasAnyAuthority("ADMIN", "USER"));

    http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable);

    http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

    http.addFilterAt(
        new UserLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
        UsernamePasswordAuthenticationFilter.class);

    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
