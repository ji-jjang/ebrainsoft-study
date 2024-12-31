package com.juny.finalboard.domain.post.gallery.common.repository;

import com.juny.finalboard.domain.post.gallery.common.entity.GalleryCategory;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GalleryCategoryRepository {

  Optional<GalleryCategory> findById(Long id);

  List<GalleryCategory> findAll();
}
