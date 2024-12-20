package com.juny.finalboard.domain.post.announcement.dto;

import lombok.Builder;

@Builder
public record SearchCondition(
    String startDate,
    String endDate,
    String categoryName,
    String keyword,
    String sort,
    int page,
    int pageSize,
    int offset) {}
