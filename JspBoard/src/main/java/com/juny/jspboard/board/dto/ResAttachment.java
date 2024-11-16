package com.juny.jspboard.board.dto;

public record ResAttachment(
    Long id,
    String logicalName,
    String logicalPath,
    String storedName,
    String storedPath,
    String extension) {}
