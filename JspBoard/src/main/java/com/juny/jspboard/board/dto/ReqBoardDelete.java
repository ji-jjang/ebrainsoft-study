package com.juny.jspboard.board.dto;

import java.util.List;

public record ReqBoardDelete(
    Long boardId,
    String password,
    String method,
    String[] deleteImages,
    String[] deleteAttachments,
    List<Long> deleteComments) {}
