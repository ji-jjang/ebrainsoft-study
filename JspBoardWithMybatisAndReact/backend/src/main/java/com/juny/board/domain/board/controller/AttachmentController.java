package com.juny.board.domain.board.controller;

import com.juny.board.domain.board.entity.Attachment;
import com.juny.board.domain.board.service.AttachmentService;
import com.juny.board.domain.utils.FileService;
import com.juny.board.domain.utils.dto.ResFileDownload;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AttachmentController {

  private final AttachmentService attachmentService;
  private final FileService fileService;

  public AttachmentController(AttachmentService attachmentService, FileService fileService) {
    this.attachmentService = attachmentService;
    this.fileService = fileService;
  }

  /**
   *
   *
   * <h1>첨부 파일 다운로드 컨트롤러 </h1>
   *
   * <br>
   * - jsp 파일에서 요청된 첨부파일 id를 받아 첨부 파일 정보를 검색 <br>
   * - 서버에 저장된 경로를 downloadService 에 전달
   *
   * @param id
   * @param res
   */
  @GetMapping("/v1/attachments/{id}/download")
  public void downloadAttachment(@PathVariable Long id, HttpServletResponse res) {

    Attachment attachment = Attachment.builder().id(id).build();

    ResFileDownload path = attachmentService.getAttachmentPath(attachment);

    fileService.responseFile(path, res);
  }
}
