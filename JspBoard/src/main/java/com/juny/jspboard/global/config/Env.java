package com.juny.jspboard.global.config;

/** 추후 Secret Key 적용할 수 있음 */
public final class Env {

  public static final String ATTACHMENT_PATH = ConfigLoader.getValue("env.attachmentPath");
  public static final String IMAGE_PATH = ConfigLoader.getValue("env.imagePath");
  public static final String DB_URL = ConfigLoader.getValue("database.url");
  public static final String DB_USER = ConfigLoader.getValue("database.user");
  public static final String DB_PASSWORD = ConfigLoader.getValue("database.password");
  public static final String DB_DRIVER = ConfigLoader.getValue("database.driver");

  private Env() {}
}
