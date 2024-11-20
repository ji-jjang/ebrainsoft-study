package com.juny.jspboardwithmybatis.domain.board.mapper;

import com.juny.jspboardwithmybatis.domain.board.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentMapper {

  Attachment findAttachmentById(Long id);

  void saveAttachment(Attachment attachment);
}
