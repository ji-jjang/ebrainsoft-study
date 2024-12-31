package com.juny.finalboard.domain.post.gallery.common.dto;

import lombok.Builder;

@Builder
public record GalleryPageInfo(
    Integer page, Integer pageSize, Integer totalPages, Long totalCount) {}
