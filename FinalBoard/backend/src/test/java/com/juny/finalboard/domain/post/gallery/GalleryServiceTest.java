package com.juny.finalboard.domain.post.gallery;

import com.juny.finalboard.domain.post.gallery.common.dto.GallerySearchCondition;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqGetGalleryPostList;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqUpdateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryCategory;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqCreateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryUpdateVO;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryCategoryService;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GalleryServiceTest {

  @Autowired
  private GalleryService galleryService;

  @Autowired private GalleryCategoryService galleryCategoryService;

  @Test
  @DisplayName("createGalleryPost")
  @Disabled
  void createGalleryPost() {

    for (int i = 2; i < 110; ++i) {
      ReqCreateGalleryPost req = ReqCreateGalleryPost.builder()
        .title("gallery title " + i)
        .content("gallery content" + i)
        .categoryId("1")
        .build();
      GalleryPost galleryPost = galleryService.createGalleryPost(req, 3L);
    }
  }

  @Test
  @DisplayName("getGalleryPostDetail")
  @Disabled
  void getGalleryPostDetail() {

    GalleryPost galleryPostDetail = galleryService.getGalleryPostDetail(1L);
  }

  @Test
  @DisplayName("getGalleryPostListBySearchCondition")
  @Disabled
  void getGalleryPostListBySearchCondition() {

    ReqGetGalleryPostList req = ReqGetGalleryPostList.builder()
      .startDate(LocalDate.of(2024, 12, 1).toString())
      .endDate(LocalDate.of(2024, 12, 31).toString())
      .build();

    GallerySearchCondition searchCondition = galleryService.createSearchCondition(req);
    System.out.println("searchCondition = " + searchCondition);
    long totalPostCountBySearchCondition = galleryService.getTotalPostCountBySearchCondition(
      searchCondition);
    System.out.println("totalPostCountBySearchCondition = " + totalPostCountBySearchCondition);
    List<GalleryPost> galleryPosts = galleryService.getGalleryPostListBySearchCondition(
      searchCondition);
  }

  @Test
  @DisplayName("updatePost")
  @Disabled
  void updatePost() {

    ReqUpdateGalleryPost req = ReqUpdateGalleryPost.builder()
      .title("gallery title update")
      .content("gallery content update")
      .categoryId("2")
      .deleteImageIds(List.of(1L))
      .build();

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(1L);

    GalleryUpdateVO galleryUpdateVO = galleryService.updatePost(req, galleryPost, 1L);
  }

  @Test
  @DisplayName("deletePost")
  @Disabled
  void deletePost() {

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(1L);
    galleryService.deletePost(1L, galleryPost, 3L);
  }

  @Test
  @DisplayName("getAllCategories")
  @Disabled
  void getAllCategories() {

    List<GalleryCategory> allCategories = galleryCategoryService.getAllCategories();
  }
}
