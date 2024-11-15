package com.juny.jspboard.constant;

public final class ErrorMessage {

  public static final String NO_HANDLER_MSG = "No handler found for url: ";
  public static final String FILE_NOT_FOUND_MSG = "File not found path: ";
  public static final String FILE_DOWNLOAD_FAIL_MSG = "File download failed: ";
  public static final String ROW_NOT_CHANGED_MSG = "Row not changed, sql: ";
  public static final String HTTP_METHOD_NOT_MATCH_MSG = "HTTP method not match: ";
  public static final Object CREATED_BY_NAME_TOO_SHORT_MSG = "Author name too short: ";
  public static final String CREATED_BY_NAME_TOO_LONG_MSG = "Author name too long: ";
  public static final String PASSWORD_TOO_SHORT_MSG = "Password too short: ";
  public static final String PASSWORD_TOO_LONG_MSG = "Password too long: ";
  public static final String TITLE_TOO_SHORT_MSG = "Title too short: ";
  public static final String TITLE_TOO_LONG_MSG = "Title too long: ";
  public static final String CONTENT_TOO_SHORT_MSG = "Content too short: ";
  public static final String CONTENT_TOO_LONG_MSG = "Content too long: ";
  public static final String INPUT_PASSWORD_NOT_MATCH_MSG = "Entered password not match.";
  public static final String PASSWORD_NOT_MATCH_MSG = "password not match.";

  private ErrorMessage() {}
}
