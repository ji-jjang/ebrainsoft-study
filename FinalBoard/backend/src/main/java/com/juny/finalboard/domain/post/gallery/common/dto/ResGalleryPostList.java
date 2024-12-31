package com.juny.finalboard.domain.post.gallery.common.dto;

import java.util.List;

public record ResGalleryPostList(
    List<ResGalleryPost> resGalleryPostList,
    GallerySearchCondition resSearchCondition,
    GalleryPageInfo resPageInfo) {}
