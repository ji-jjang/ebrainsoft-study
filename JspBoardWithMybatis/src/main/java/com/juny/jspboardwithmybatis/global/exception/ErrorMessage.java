package com.juny.jspboardwithmybatis.global.exception;

public class ErrorMessage {

  public static final String ATTACHMENT_NOT_FOUND_MSG = "Attachment not found";
  public static final String BOARD_NOT_FOUND_MSG = "Board not found";
  public static final String INTERNAL_SERVER_ERROR_MSG = "Internal server error";
  public static final String INVALID_PARAMETER_MSG = "Invalid parameter";
  public static final String PARAMETER_NULL_OR_EMPTY = "Parameter cannot be null or empty";
  public static final String INVALID_PAGE_PARAMETER = "Page param cannot be negative";
  public static final String PASSWORD_NOT_MATCH = "Password does not match";
  public static final String UNLINKED_IMAGE_DELETE_MSG =
      "Images not associated with the board cannot be deleted";
  public static final String UNLINKED_ATTACHMENT_DELETE_MSG =
      "Attachments not associated with the board cannot be deleted";
  public static final String FILE_NOT_FOUND_MSG = "File not found";
  public static final String FILE_INPUT_OUTPUT_ERROR = "File input/output error";
}
