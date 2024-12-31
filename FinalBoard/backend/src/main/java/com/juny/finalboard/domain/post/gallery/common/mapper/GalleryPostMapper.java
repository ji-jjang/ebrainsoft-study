package com.juny.finalboard.domain.post.gallery.common.mapper;

import com.juny.finalboard.domain.post.gallery.common.dto.GallerySearchCondition;
import com.juny.finalboard.domain.post.gallery.common.dto.ResGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryPost;
import java.time.LocalDateTime;
import java.util.List;

public class GalleryPostMapper {

  public static ResGalleryPost toResGalleryPost(GalleryPost galleryPost) {

    boolean isNew = LocalDateTime.now().minusDays(7).isBefore(galleryPost.getCreatedAt());

    int imageCount =
        galleryPost.getGalleryImages() == null ? 0 : galleryPost.getGalleryImages().size();

    String representImagePath = "";
    if (imageCount > 0) {
      GalleryImage image = galleryPost.getGalleryImages().getFirst();
      representImagePath = image.getStoredPath() + image.getStoredName() + image.getExtension();
    }

    return ResGalleryPost.builder()
        .id(galleryPost.getId())
        .title(galleryPost.getTitle())
        .content(galleryPost.getContent())
        .viewCount(galleryPost.getViewCount())
        .createdAt(galleryPost.getCreatedAt().toString())
        .createdBy(galleryPost.getCreatedBy())
        .isNew(isNew)
        .imageCount(imageCount)
        .representImagePath(representImagePath)
        .categories(galleryPost.getGalleryCategory())
        .galleryImages(galleryPost.getGalleryImages())
        .build();
  }

  public static List<ResGalleryPost> toResGalleryPostList(
      List<GalleryPost> galleryPosts, GallerySearchCondition searchCondition, long totalPostCount) {
    return null;
  }
}
