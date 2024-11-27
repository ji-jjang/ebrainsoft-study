package com.juny.board.global.exception.hierachy.attachment;

import com.juny.board.global.exception.BusinessException;
import com.juny.board.global.exception.ErrorCode;

public class AttachmentNotFoundException extends BusinessException {

  public AttachmentNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}