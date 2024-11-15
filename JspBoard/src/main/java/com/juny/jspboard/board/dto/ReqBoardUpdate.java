package com.juny.jspboard.board.dto;

import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.BoardImage;
import java.util.List;

public record ReqBoardUpdate(
    Long boardId,
    String createdBy,
    String title,
    String updatedAt,
    String content,
    String password,
    String method,
    List<String> deleteAttachments,
    List<String> deleteImages,
    List<BoardImage> boardImages,
    List<Attachment> attachments)
    implements ReqBoardForm {}
