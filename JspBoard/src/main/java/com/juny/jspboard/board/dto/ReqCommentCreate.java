package com.juny.jspboard.board.dto;

public record ReqCommentCreate(
    Long boardId, String name, String password, String content, String method) {}
