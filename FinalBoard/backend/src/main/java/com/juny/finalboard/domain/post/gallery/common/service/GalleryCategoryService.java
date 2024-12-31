package com.juny.finalboard.domain.post.gallery.common.service;

import com.juny.finalboard.domain.post.gallery.common.entity.GalleryCategory;
import com.juny.finalboard.domain.post.gallery.common.repository.GalleryCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GalleryCategoryService {

  private final GalleryCategoryRepository galleryCategoryRepository;

  public List<GalleryCategory> getAllCategories() {

    return galleryCategoryRepository.findAll();
  }
}
