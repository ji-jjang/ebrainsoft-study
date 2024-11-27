package com.juny.board.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

  private final HttpStatus httpStatus;
  private final String msg;

  public ErrorResponse(ErrorCode errorCode) {
    this.httpStatus = errorCode.getHttpStatus();
    this.msg = errorCode.getMsg();
  }

  public ErrorResponse(ErrorCode errorCode, String msg) {
    this.httpStatus = errorCode.getHttpStatus();
    this.msg = msg;
  }
}
