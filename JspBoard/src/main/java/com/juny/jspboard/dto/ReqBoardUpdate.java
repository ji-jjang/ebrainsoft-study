package com.juny.jspboard.dto;

import com.juny.jspboard.entity.Attachment;
import com.juny.jspboard.entity.BoardImage;
import java.util.List;

public record ReqBoardUpdate(
  Long boardId,
  String createdBy,
  String updatedTitle,
  String updatedAt,
  String content,
  List<String> deleteAttachments,
  List<String> deleteImages,
  List<BoardImage> boardImages,
  List<Attachment> attachments
) {}
