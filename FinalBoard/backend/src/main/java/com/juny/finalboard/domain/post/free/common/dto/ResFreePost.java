package com.juny.finalboard.domain.post.free.common.dto;

import com.juny.finalboard.domain.post.free.common.entity.FreeAttachment;
import com.juny.finalboard.domain.post.free.common.entity.FreeComment;
import java.util.List;
import lombok.Builder;

@Builder
public record ResFreePost(
    Long id,
    String title,
    String content,
    Integer viewCount,
    String createdBy,
    String createdAt,
    Long categoryId,
    String categoryName,
    Boolean isNew,
    Boolean hasAttachment,
    Integer commentCount,
    List<FreeAttachment> attachmentList,
    List<FreeComment> commentList,
    Long userId) {}
