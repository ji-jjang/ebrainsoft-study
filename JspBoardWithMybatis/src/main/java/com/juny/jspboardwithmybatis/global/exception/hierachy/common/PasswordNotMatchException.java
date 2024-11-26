package com.juny.jspboardwithmybatis.global.exception.hierachy.common;

import com.juny.jspboardwithmybatis.global.exception.BusinessException;
import com.juny.jspboardwithmybatis.global.exception.ErrorCode;

public class PasswordNotMatchException extends BusinessException {

  public PasswordNotMatchException(ErrorCode errorCode) {
    super(errorCode);
  }

  public PasswordNotMatchException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
