package com.juny.board.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${RESOURCE_IMAGE_PATH}")
  private String resourceImagePath;

  @Value("${RESOURCE_ATTACHMENT_PATH}")
  private String resourceAttachmentPath;

  /**
   *
   *
   * <h1>React Cors 설정 </h1>
   *
   * @param registry
   */
  public void addCorsMappings(CorsRegistry registry) {

    registry
        .addMapping("/**")
        .allowedHeaders("*")
        .allowedMethods("*")
        .allowedOrigins("http://localhost:5173")
        .allowCredentials(true);
  }

  /**
   *
   *
   * <h1>정적 리소스 경로 매핑 </h1>
   *
   * <br>
   * - SSR 아니므로 해당 설정이 필요하다!
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry
        .addResourceHandler("/images/**")
        .addResourceLocations(resourceImagePath);

    registry
        .addResourceHandler("/attachments/**")
        .addResourceLocations(resourceAttachmentPath);
  }
}
