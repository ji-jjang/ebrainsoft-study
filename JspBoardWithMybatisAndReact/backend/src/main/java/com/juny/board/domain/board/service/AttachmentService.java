package com.juny.board.domain.board.service;

import com.juny.board.domain.board.entity.Attachment;
import com.juny.board.domain.board.repository.AttachmentRepository;
import com.juny.board.domain.utils.dto.ResFileDownload;
import com.juny.board.global.Constants;
import com.juny.board.global.exception.ErrorCode;
import com.juny.board.global.exception.hierachy.attachment.AttachmentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentService {

  private final AttachmentRepository attachmentRepository;

  /**
   *
   *
   * <h1>attachment 엔티티 조회하여 경로 반환 </h1>
   *
   * @param attachment
   * @return ResFileDownload (FilePath, logicalName)
   */
  public ResFileDownload getAttachmentPath(Attachment attachment) {

    Attachment att = getAttachment(attachment);

    String path =
        att.getStoredPath() + att.getStoredName() + Constants.DOT_SIGN + att.getExtension();
    String logicalName = att.getLogicalName();

    return new ResFileDownload(path, logicalName);
  }

  /**
   *
   *
   * <h1>attachment id를 받아 엔티티 조회 </h1>
   *
   * @return attachment
   */
  public Attachment getAttachment(Attachment attachment) {

    Attachment att = attachmentRepository.findAttachmentById(attachment.getId());

    if (att == null) {
      throw new AttachmentNotFoundException(ErrorCode.ATTACHMENT_NOT_FOUND);
    }
    return att;
  }
}
