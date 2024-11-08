package com.juny.jspboard.board.dto;

public record ResAttachment(
    Long id, String logicalName, String storedName, String storedPath, String extension) {}
