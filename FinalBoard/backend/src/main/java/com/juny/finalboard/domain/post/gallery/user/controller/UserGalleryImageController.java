package com.juny.finalboard.domain.post.gallery.user.controller;

import com.juny.finalboard.domain.post.common.LocalFileService;
import com.juny.finalboard.domain.post.free.common.dto.FileDownloadVo;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryImageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserGalleryImageController {

  private final GalleryImageService galleryImageService;
  private final LocalFileService localFileService;

  @GetMapping("/v1/images/{imageId}/download")
  public void downloadAttachment(@PathVariable Long imageId, HttpServletResponse res) {

    FileDownloadVo fileDownloadVO = galleryImageService.getImageStoredPath(imageId);

    localFileService.downloadFile(fileDownloadVO, res);
  }
}
