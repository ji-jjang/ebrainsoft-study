package com.juny.finalboard.domain.post.free.common.dto;

import lombok.Builder;

@Builder
public record FreeSearchCondition(
    String startDate,
    String endDate,
    String categoryId,
    String keyword,
    String sort,
    int page,
    int pageSize) {}
