package com.juny.finalboard.domain.post.gallery.common.entity;

import java.util.List;

public record GalleryUpdateVO(
    GalleryPost updateGalleryPost,
    List<GalleryImage> addGalleryImages,
    List<GalleryImage> deleteImages) {}
