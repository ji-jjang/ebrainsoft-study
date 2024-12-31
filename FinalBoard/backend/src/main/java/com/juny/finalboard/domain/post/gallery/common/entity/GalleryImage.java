package com.juny.finalboard.domain.post.gallery.common.entity;

import com.juny.finalboard.domain.post.common.FileDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class GalleryImage implements FileDetails {

  private Long id;
  private String logicalName;
  private String storedPath;
  private String storedName;
  private String extension;
  private Long size;

  private GalleryPost post;
}
