package com.juny.jspboard.global.config;

import com.juny.jspboard.global.constant.ErrorMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

  private static final Properties properties = new Properties();
  private static final String propertyPath = "application.properties";

  /** Property 파일 읽어옴 /resources/application.properties */
  static {
    try (InputStream inputStream =
        ConfigLoader.class.getClassLoader().getResourceAsStream(propertyPath)) {
      if (inputStream == null) {
        throw new RuntimeException(ErrorMessage.PROPERTY_FILE_NOT_FOUND_MSG + propertyPath);
      }
      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(ErrorMessage.PROPERTY_FILE_LOAD_FAILED_MSG);
    }
  }

  public static String getValue(String key) {
    return properties.getProperty(key);
  }
}
