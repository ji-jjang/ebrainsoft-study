package com.juny.jspboardwithmybatis.global.exception.hierachy.attachment;

import com.juny.jspboardwithmybatis.global.exception.BusinessException;
import com.juny.jspboardwithmybatis.global.exception.ErrorCode;

public class AttachmentNotFoundException extends BusinessException {

  public AttachmentNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
