package com.juny.finalboard.domain.post.question.common.dto;

import lombok.Builder;

@Builder
public record ReqDeleteQuestionPost(String password) {

  public ReqDeleteQuestionPost {
    if (password == null) {
      password = "";
    }
  }
}
