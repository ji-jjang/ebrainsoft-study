package com.juny.board.domain.board.repository;

import com.juny.board.domain.board.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentRepository {

  Attachment findAttachmentById(Long id);

  void saveAttachment(Attachment attachment);

  void deleteAttachmentById(Long id);
}
