package com.juny.finalboard.domain.post.free.user.controller;

import com.juny.finalboard.domain.post.free.common.dto.FileDownloadVo;
import com.juny.finalboard.domain.post.free.common.service.FreeAttachmentService;
import com.juny.finalboard.domain.post.common.LocalFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserFreeAttachmentController {

  private final FreeAttachmentService freeAttachmentService;
  private final LocalFileService localFileService;

  /**
   *
   *
   * <h1>첨부파일 다운로드 </h1>
   *
   * @param attachmentId 첨부파일 아이디
   * @param res HttpServletResponse
   */
  @GetMapping("/v1/attachments/{attachmentId}/download")
  public void downloadAttachment(@PathVariable Long attachmentId, HttpServletResponse res) {

    FileDownloadVo fileDownloadVO = freeAttachmentService.getAttachmentStoredPath(attachmentId);

    localFileService.downloadFile(fileDownloadVO, res);
  }
}
