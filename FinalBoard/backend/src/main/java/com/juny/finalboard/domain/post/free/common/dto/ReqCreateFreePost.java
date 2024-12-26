package com.juny.finalboard.domain.post.free.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ReqCreateFreePost(
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
    List<MultipartFile> attachments) {

  public ReqCreateFreePost {
    if (attachments == null || attachments.isEmpty()) {
      attachments = Collections.emptyList();
    }

    if (attachments.size() > 5) {
      throw new RuntimeException(
          String.format("max file upload limit exceeded, max:%d cur:%d", 5, attachments.size()));
    }
  }
}
