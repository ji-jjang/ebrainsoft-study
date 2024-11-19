package com.juny.jspboardwithmybatis.domain.board.service;

import com.juny.jspboardwithmybatis.domain.board.entity.Attachment;
import com.juny.jspboardwithmybatis.domain.board.mapper.AttachmentMapper;
import org.springframework.stereotype.Service;

@Service
public class AttachmentService {

  private final AttachmentMapper attachmentMapper;

  public AttachmentService(AttachmentMapper attachmentMapper) {
    this.attachmentMapper = attachmentMapper;
  }

  /**
   * <h1> attachment 엔티티 조회하여 경로 반환 </h1>
   * @param id
   * @return path
   */
  public String getAttachmentPath(Long id) {

    Attachment attachment = getAttachment(id);

    return attachment.getStoredPath()
      + attachment.getStoredName()
      + attachment.getExtension();
  }

  /**
   * <h1> attachment id를 받아 엔티티 조회 </h1>
   * @param id
   * @return attachment
   */
  public Attachment getAttachment(Long id) {

    Attachment attachment = attachmentMapper.findAttachmentById(id);

    if (attachment == null) {
      throw new RuntimeException();
    }
    return attachment;
  }
}