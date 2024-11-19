package com.juny.jspboardwithmybatis.domain.board.controller;

import com.juny.jspboardwithmybatis.domain.board.service.AttachmentService;
import com.juny.jspboardwithmybatis.domain.util.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AttachmentController {

  private final AttachmentService attachmentService;
  private final DownloadService downloadService;

  public AttachmentController(AttachmentService attachmentService, DownloadService downloadService) {
    this.attachmentService = attachmentService;
    this.downloadService = downloadService;
  }

  /**
   * <h1> 첨부 파일 다운로드 컨트롤러 </h1>
   * <br>- jsp 파일에서 요청된 첨부파일 id를 받아 첨부 파일 정보를 검색한다
   * <br>- 서버에 저장된 경로를 downloadService 에 전달한다
   * @param id
   * @param res
   */
  @GetMapping("/attachments/{id}/download")
  @ResponseBody
  public void downloadAttachment(@PathVariable Long id, HttpServletResponse res) {

    String path = attachmentService.getAttachmentPath(id);

    downloadService.responseFile(path, res);
  }
}
