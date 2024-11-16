package com.juny.jspboard.board.dto;

public record ReqBoardList(
    String startDate,
    String endDate,
    String categoryName,
    String keyword,
    int page,
    String method) {}
