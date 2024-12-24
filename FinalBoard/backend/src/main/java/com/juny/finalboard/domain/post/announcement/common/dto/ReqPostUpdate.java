package com.juny.finalboard.domain.post.announcement.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ReqPostUpdate(
    @NotNull(message = "title not null")
        @NotEmpty(message = "title not empty")
        @Size(min = 4, max = 99, message = "title must be between 4 and 99 characters")
        String title,
    @NotNull(message = "content not null")
        @NotEmpty(message = "content not empty")
        @Size(min = 4, max = 3999, message = "title must be between 4 and 99 characters")
        String content,
    Boolean isPinned,
    @NotNull(message = "categoryId not null") Long categoryId) {}
