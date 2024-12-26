package com.juny.finalboard.domain.post.free.common.dto;

import lombok.Builder;

@Builder
public record FreePageInfo(Integer page, Integer pageSize, Integer totalPages, Long totalCount) {}
