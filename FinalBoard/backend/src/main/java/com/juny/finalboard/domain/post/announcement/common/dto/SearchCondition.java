package com.juny.finalboard.domain.post.announcement.common.dto;

import lombok.Builder;

@Builder
public record SearchCondition(
    String startDate,
    String endDate,
    String categoryId,
    String keyword,
    String sort,
    int page,
    int pageSize) {}
