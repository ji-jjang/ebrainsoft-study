package com.juny.jspboard.board.dto;

import java.util.List;

public record ResBoardDetail(
    Long id,
    String title,
    String content,
    int viewCount,
    String createdAt,
    String createdBy,
    String updatedAt,
    String categoryName,
    List<ResBoardImage> boardImages,
    List<ResAttachment> attachments,
    List<ResComment> comments) {}
