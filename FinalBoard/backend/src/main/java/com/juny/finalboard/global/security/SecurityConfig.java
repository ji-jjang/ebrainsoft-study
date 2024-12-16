package com.juny.finalboard.global.security;

import com.juny.finalboard.global.security.admin.Filter.AdminLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {

    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {

    return configuration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain adminFilterChain(
      HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {

    http.authorizeHttpRequests(
        (auth) ->
            auth.requestMatchers("/admin/login")
                .permitAll()
                .requestMatchers("/admin/**")
                .hasAuthority("ADMIN"));

    http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable);

    http.addFilterAt(
        new AdminLoginFilter(authenticationManager(authenticationConfiguration)),
        UsernamePasswordAuthenticationFilter.class);

    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

}
