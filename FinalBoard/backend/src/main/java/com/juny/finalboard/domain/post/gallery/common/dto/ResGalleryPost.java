package com.juny.finalboard.domain.post.gallery.common.dto;

import com.juny.finalboard.domain.post.gallery.common.entity.GalleryCategory;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import java.util.List;
import lombok.Builder;

@Builder
public record ResGalleryPost(
    Long id,
    String title,
    String content,
    Integer viewCount,
    String createdAt,
    String createdBy,
    boolean isNew,
    Integer imageCount,
    Long userId,
    String representImagePath,
    GalleryCategory categories,
    List<GalleryImage> galleryImages) {}
