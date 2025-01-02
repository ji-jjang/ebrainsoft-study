package com.juny.finalboard.domain.post.question.common.dto;

import lombok.Builder;

@Builder
public record QuestionSearchCondition(
    String startDate,
    String endDate,
    String categoryId,
    String keyword,
    String sort,
    int page,
    int pageSize) {}
