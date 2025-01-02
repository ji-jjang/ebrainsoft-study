package com.juny.finalboard.domain.post.question.common.dto;

import lombok.Builder;

@Builder
public record QuestionPageInfo(
    Integer page, Integer pageSize, Integer totalPages, Long totalCount) {}
