package com.juny.finalboard.domain.post.question.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ReqUpdateQuestionPost(
    @NotNull(message = "title not null")
        @NotEmpty(message = "title not empty")
        @Size(min = 4, max = 15, message = "title min 4, max 99")
        String title,
    @NotNull(message = "content not null")
        @NotEmpty(message = "content not empty")
        @Size(min = 4, max = 3999, message = "content must be between 4 and 3999 characters")
        String content,
    @NotNull(message = "categoryId not null") @NotEmpty(message = "categoryId not empty")
        String categoryId,
    Boolean isSecret,
    String password) {

  public ReqUpdateQuestionPost {

    if (categoryId == null) {
      categoryId = "";
    }

    if (isSecret == null) {
      isSecret = false;
    }

    if (password == null) {
      password = "";
    }
  }
}
