package com.juny.finalboard.domain.post.gallery.common.dto;

import lombok.Builder;

@Builder
public record GallerySearchCondition(
    String startDate,
    String endDate,
    String categoryId,
    String keyword,
    String sort,
    int page,
    int pageSize) {}
