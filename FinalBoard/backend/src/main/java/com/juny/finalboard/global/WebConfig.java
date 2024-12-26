package com.juny.finalboard.global;

import com.juny.finalboard.global.constant.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${resources.image-resource}")
  private String imageResourcePath;

  @Value("${resources.attachment-resource}")
  private String attachmentResourcePath;

  /**
   *
   *
   * <h1>CORS 설정 </h1>
   *
   * @param registry 레지스트리
   */
  public void addCorsMappings(CorsRegistry registry) {

    registry
        .addMapping("/**")
        .allowedOrigins("http://localhost:5173")
        .allowedHeaders("*")
        .allowedMethods("*")
        .allowCredentials(false);
  }

  /**
   *
   *
   * <h1>정적 리소스 경로 매핑 </h1>
   *
   * <br>
   * - React 접근하기 위한 정적 리소스 경로
   *
   * @param registry 레지스트리
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/images/**").addResourceLocations(Constants.FILE_RESOURCE_PREFIX + imageResourcePath);

    registry.addResourceHandler("/attachments/**").addResourceLocations(Constants.FILE_RESOURCE_PREFIX + attachmentResourcePath);
  }
}
