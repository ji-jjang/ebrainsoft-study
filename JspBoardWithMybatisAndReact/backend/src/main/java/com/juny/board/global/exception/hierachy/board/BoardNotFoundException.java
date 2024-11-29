package com.juny.board.global.exception.hierachy.board;

import com.juny.board.global.exception.BusinessException;
import com.juny.board.global.exception.ErrorCode;

public class BoardNotFoundException extends BusinessException {

  public BoardNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
