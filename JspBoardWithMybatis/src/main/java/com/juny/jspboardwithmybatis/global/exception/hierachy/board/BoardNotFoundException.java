package com.juny.jspboardwithmybatis.global.exception.hierachy.board;

import com.juny.jspboardwithmybatis.global.exception.BusinessException;
import com.juny.jspboardwithmybatis.global.exception.ErrorCode;

public class BoardNotFoundException extends BusinessException {

  public BoardNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
