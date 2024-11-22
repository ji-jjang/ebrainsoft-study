package com.juny.jspboardwithmybatis.global.exception.hierachy.common;

import com.juny.jspboardwithmybatis.global.exception.BusinessException;
import com.juny.jspboardwithmybatis.global.exception.ErrorCode;

public class RequestParameterInvalidException extends BusinessException {

  public RequestParameterInvalidException(ErrorCode errorCode) {
    super(errorCode);
  }

  public RequestParameterInvalidException(ErrorCode errorCode, String msg) {
    super(errorCode, msg);
  }
}
