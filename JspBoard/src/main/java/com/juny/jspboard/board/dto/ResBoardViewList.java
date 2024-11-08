package com.juny.jspboard.board.dto;

public record ResBoardViewList(
    String categoryName,
    Long id,
    boolean hasAttachment,
    String title,
    String createdBy,
    Integer viewCount,
    String createdAt,
    String updatedAt) {}
