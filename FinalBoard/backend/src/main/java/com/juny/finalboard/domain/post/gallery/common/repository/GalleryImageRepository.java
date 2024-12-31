package com.juny.finalboard.domain.post.gallery.common.repository;

import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GalleryImageRepository {

  List<GalleryImage> findImagesByPostIds(List<Long> postIds);

  void save(GalleryImage galleryImage);

  Optional<GalleryImage> findById(Long id);

  void deleteById(Long id);
}
