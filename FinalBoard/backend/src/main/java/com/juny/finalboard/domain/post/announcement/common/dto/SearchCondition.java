package com.juny.finalboard.domain.post.announcement.common.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record SearchCondition(
    String startDate,
    String endDate,
    Long categoryId,
    String keyword,
    String sort,
    int page,
    int pageSize,
    int offset) {}
