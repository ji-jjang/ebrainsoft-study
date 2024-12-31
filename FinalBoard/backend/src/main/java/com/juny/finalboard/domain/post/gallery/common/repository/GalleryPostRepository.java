package com.juny.finalboard.domain.post.gallery.common.repository;

import com.juny.finalboard.domain.post.gallery.common.dto.GallerySearchCondition;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryPost;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GalleryPostRepository {

  void save(GalleryPost galleryPost);

  void deletePostById(Long postId);

  Long getTotalGalleryPostCountBySearchCondition(
      @Param("searchCondition") GallerySearchCondition searchCondition,
      @Param("offset") int offset);

  List<GalleryPost> findAllWithBySearchCondition(
      @Param("searchCondition") GallerySearchCondition searchCondition,
      @Param("offset") int offset);

  Optional<GalleryPost> findGalleryPostDetailById(Long postId);

  void increaseViewCount(Long postId);

  void updatePost(GalleryPost galleryPost);
}
