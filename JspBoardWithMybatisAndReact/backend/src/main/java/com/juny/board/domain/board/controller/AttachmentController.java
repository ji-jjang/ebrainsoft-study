package com.juny.board.domain.board.controller;

import com.juny.board.domain.board.service.AttachmentService;
import com.juny.board.domain.utils.FileUtils;
import com.juny.board.domain.utils.dto.ResFileDownload;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AttachmentController {

  private final AttachmentService attachmentService;

  public AttachmentController(AttachmentService attachmentService) {
    this.attachmentService = attachmentService;
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
  @GetMapping("/attachments/{id}/download")
  @ResponseBody
  public void downloadAttachment(@PathVariable Long id, HttpServletResponse res) {

    ResFileDownload path = attachmentService.getAttachmentPath(id);

    FileUtils.responseFile(path, res);
  }
}
