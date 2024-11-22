package com.juny.jspboardwithmybatis.global.exception;

import com.juny.jspboardwithmybatis.global.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  INTERNAL_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR,
      Constants.INTERNAL_SERVER_ERROR_CODE,
      ErrorMessage.INTERNAL_SERVER_ERROR_MSG),

  REQUEST_PARAMETER_INVALID(
      HttpStatus.BAD_REQUEST, Constants.INVALID_PARAM_CODE, ErrorMessage.INVALID_PARAMETER_MSG),

  PASSWORD_NOT_MATCH(
      HttpStatus.BAD_REQUEST, Constants.PASSWORD_NOT_MATCH_CODE, ErrorMessage.PASSWORD_NOT_MATCH),

  BOARD_NOT_FOUND(
      HttpStatus.BAD_REQUEST, Constants.BOARD_NOT_FOUND_CODE, ErrorMessage.BOARD_NOT_FOUND_MSG),

  ATTACHMENT_NOT_FOUND(
      HttpStatus.BAD_REQUEST,
      Constants.ATTACHMENT_NOT_FOUND_CODE,
      ErrorMessage.ATTACHMENT_NOT_FOUND_MSG);

  private final HttpStatus httpStatus;
  private final String code;
  private final String msg;
}
