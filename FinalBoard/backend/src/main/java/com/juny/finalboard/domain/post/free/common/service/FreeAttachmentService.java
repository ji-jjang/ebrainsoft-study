package com.juny.finalboard.domain.post.free.common.service;

import com.juny.finalboard.domain.post.free.common.dto.FileDownloadVo;
import com.juny.finalboard.domain.post.free.common.entity.FreeAttachment;
import com.juny.finalboard.domain.post.free.common.repository.FreeAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreeAttachmentService {

  private final FreeAttachmentRepository freeAttachmentRepository;

  /**
   *
   *
   * <h1>첨부파일 저장된 경로 및 사용자가 등록한 파일 이름 변환 </h1>
   *
   * @param attachmentId 첨부파일 아이디
   * @return FileDownloadVO
   */
  public FileDownloadVo getAttachmentStoredPath(Long attachmentId) {

    FreeAttachment attachment =
        freeAttachmentRepository
            .findById(attachmentId)
            .orElseThrow(
                () ->
                    new RuntimeException(String.format("invalid attachment id: %d", attachmentId)));

    String storedPathAndFileName =
        attachment.getStoredPath() + attachment.getStoredName() + attachment.getExtension();

    return new FileDownloadVo(storedPathAndFileName, attachment.getLogicalName());
  }
}
