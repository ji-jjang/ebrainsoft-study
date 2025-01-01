package com.juny.finalboard.domain.post.gallery.common.service;

import com.juny.finalboard.domain.post.free.common.dto.FileDownloadVo;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import com.juny.finalboard.domain.post.gallery.common.repository.GalleryImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GalleryImageService {

  private final GalleryImageRepository galleryImageRepository;

  /**
   *
   *
   * <h1>수정화면에서 이미지 파일 다운로드 </h1>
   *
   * @param imageId 이미지 아이디
   * @return 이미지 저장 경로
   */
  public FileDownloadVo getImageStoredPath(Long imageId) {

    GalleryImage galleryImage =
        galleryImageRepository
            .findById(imageId)
            .orElseThrow(() -> new RuntimeException(String.format("invalid image id %d", imageId)));

    String storedPathAndFileName =
        galleryImage.getStoredPath() + galleryImage.getStoredName() + galleryImage.getExtension();

    return new FileDownloadVo(storedPathAndFileName, galleryImage.getLogicalName());
  }
}
