package com.juny.finalboard.domain.post.announcement.dto;

import lombok.Builder;

@Builder
public record PageInfo(Integer page, Integer pageSize, Integer totalPages, Long totalCount) {}
