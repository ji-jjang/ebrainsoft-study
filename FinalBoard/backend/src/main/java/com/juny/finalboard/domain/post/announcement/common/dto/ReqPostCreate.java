package com.juny.finalboard.domain.post.announcement.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ReqPostCreate(
    @NotNull(message = "title not null") @NotEmpty(message = "title not empty") String title,
    @NotNull(message = "content not null") @NotEmpty(message = "content not empty") String content,
    Boolean isPinned,
    @NotNull(message = "categoryId not null") Long categoryId) {}
