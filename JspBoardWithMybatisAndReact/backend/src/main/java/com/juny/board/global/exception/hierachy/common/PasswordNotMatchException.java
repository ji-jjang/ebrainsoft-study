package com.juny.board.global.exception.hierachy.common;

import com.juny.board.global.exception.BusinessException;
import com.juny.board.global.exception.ErrorCode;

public class PasswordNotMatchException extends BusinessException {

  public PasswordNotMatchException(ErrorCode errorCode) {
    super(errorCode);
  }

  public PasswordNotMatchException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
