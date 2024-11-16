package com.juny.jspboard.board.dto;

import java.util.List;
import java.util.Map;

public record ResBoardList(
    Map<String, String> searchConditions,
    int totals,
    int totalPages,
    List<ResBoardViewList> boards,
    List<String> categories,
    String defaultStartDate,
    String defaultEndDate) {}
