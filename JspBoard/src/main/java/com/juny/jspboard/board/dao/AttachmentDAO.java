package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.entity.Attachment;
import java.sql.Connection;
import java.util.List;

public interface AttachmentDAO {

  boolean existsAttachment(Long boardId);

  List<Attachment> getAttachmentsByBoardId(Connection conn, Long boardId);

  List<Attachment> getAttachmentsByBoardId(Long boardId);

  void deleteAttachmentById(Connection conn, Long attachmentId);

  void saveAttachment(Connection conn, Attachment attachment);
}
