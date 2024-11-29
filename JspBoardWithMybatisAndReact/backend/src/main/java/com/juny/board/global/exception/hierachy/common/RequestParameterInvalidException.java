package com.juny.board.global.exception.hierachy.common;

import com.juny.board.global.exception.BusinessException;
import com.juny.board.global.exception.ErrorCode;

public class RequestParameterInvalidException extends BusinessException {

  public RequestParameterInvalidException(ErrorCode errorCode) {
    super(errorCode);
  }

  public RequestParameterInvalidException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
