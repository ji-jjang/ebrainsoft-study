package com.juny.finalboard.domain.post.gallery.common.mapper;

import com.juny.finalboard.domain.post.gallery.common.dto.GalleryPageInfo;
import com.juny.finalboard.domain.post.gallery.common.dto.GallerySearchCondition;
import com.juny.finalboard.domain.post.gallery.common.dto.ResGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ResGalleryPostList;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryPost;
import com.juny.finalboard.global.constant.Constants;
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
      representImagePath = image.getStoredName() + image.getExtension();
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

  public static ResGalleryPostList toResGalleryPostList(
      List<GalleryPost> galleryPosts, GallerySearchCondition searchCondition, long totalPostCount) {

    String[] sort = searchCondition.sort().split(Constants.SPACE_SIGN);

    GalleryPageInfo resPageInfo = GalleryPageInfo.builder()
      .page(searchCondition.page() + 1)
      .pageSize(searchCondition.pageSize())
      .totalPages((int) Math.ceil((double) totalPostCount / searchCondition.pageSize()))
      .totalCount(totalPostCount)
      .build();

    GallerySearchCondition resSearchCondition = GallerySearchCondition.builder()
      .startDate(searchCondition.startDate().split(Constants.SPACE_SIGN)[0])
      .endDate(searchCondition.endDate().split(Constants.SPACE_SIGN)[0])
      .categoryId(searchCondition.categoryId())
      .keyword(searchCondition.keyword())
      .pageSize(searchCondition.pageSize())
      .sort(sort[0] + Constants.COLON_SIGN + sort[1])
      .build();

    List<ResGalleryPost> resGalleryPostList = galleryPosts.stream().map(GalleryPostMapper::toResGalleryPost)
      .toList();

    return new ResGalleryPostList(resGalleryPostList, resSearchCondition, resPageInfo);
  }
}
