package com.juny.finalboard.domain.post.question.common.dto;

import com.juny.finalboard.global.constant.Constants;
import lombok.Builder;

@Builder
public record ReqGetQuestionPost(String password) {

  public ReqGetQuestionPost {
    if (password == null) {
      password = Constants.EMPTY_SIGN;
    }
  }
}
