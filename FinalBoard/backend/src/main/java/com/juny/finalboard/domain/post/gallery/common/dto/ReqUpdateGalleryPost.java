package com.juny.finalboard.domain.post.gallery.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ReqUpdateGalleryPost(
    @NotNull(message = "title not null")
        @NotEmpty(message = "title not empty")
        @Size(min = 4, max = 15, message = "title min 4, max 99")
        String title,
    @NotNull(message = "content not null")
        @NotEmpty(message = "content not empty")
        @Size(min = 4, max = 3999, message = "content must be between 4 and 3999 characters")
        String content,
    String categoryId,
    @Size(max = 5, message = "max file upload limit exceeded, max: {max} files")
        List<MultipartFile> addImages,
    List<Long> deleteImageIds) {

  public ReqUpdateGalleryPost {

    if (categoryId == null) {
      categoryId = "";
    }

    if (addImages == null) {
      addImages = Collections.emptyList();
    }

    if (addImages == null) {
      addImages = Collections.emptyList();
    }

    if (deleteImageIds == null) {
      deleteImageIds = Collections.emptyList();
    }

    List<MultipartFile> newImages = new ArrayList<>();

    for (var image : addImages) {
      if (image.getSize() > 0) newImages.add(image);
    }

    if (newImages.size() > 5) {
      throw new RuntimeException(
          String.format("max file upload limit exceeded, max:%d cur:%d", 5, newImages.size()));
    }

    addImages = newImages;
  }
}
