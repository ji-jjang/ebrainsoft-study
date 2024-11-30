package com.juny.board.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedHeaders("*")
        .allowedMethods("*")
        .allowedOrigins("http://localhost:5173")
        .allowCredentials(true);
  }
}
