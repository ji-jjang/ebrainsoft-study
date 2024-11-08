package com.juny.jspboard.board.dto;

public record ReqBoardCreate(
    String category,
    String createdBy,
    String password,
    String passwordConfirm,
    String title,
    String content,
    String method)
    implements ReqBoardForm {}
