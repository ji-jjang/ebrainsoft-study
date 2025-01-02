package com.juny.finalboard.domain.post.question.common.dto;

import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import lombok.Builder;

@Builder
public record ResQuestionPost(
    Long id,
    String title,
    String content,
    boolean isSecret,
    Integer viewCount,
    String createdAt,
    String createdBy,
    boolean isNew,
    Long userId,
    QuestionCategory category) {}
