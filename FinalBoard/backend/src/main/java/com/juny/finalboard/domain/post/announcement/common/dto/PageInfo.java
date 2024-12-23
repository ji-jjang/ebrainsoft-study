package com.juny.finalboard.domain.post.announcement.common.dto;

import lombok.Builder;

@Builder
public record PageInfo(Integer page, Integer pageSize, Integer totalPages, Long totalCount) {}
