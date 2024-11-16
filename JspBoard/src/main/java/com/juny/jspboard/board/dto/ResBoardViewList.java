package com.juny.jspboard.board.dto;

public record ResBoardViewList(
    Long id,
    String title,
    Integer viewCount,
    String createdAt,
    String createdBy,
    String updatedAt,
    String categoryName,
    boolean hasAttachment) {}
