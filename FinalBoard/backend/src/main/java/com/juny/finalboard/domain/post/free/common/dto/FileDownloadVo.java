package com.juny.finalboard.domain.post.free.common.dto;

import lombok.Builder;

@Builder
public record FileDownloadVo(String storedPathAndFileName, String logicalName) {}
