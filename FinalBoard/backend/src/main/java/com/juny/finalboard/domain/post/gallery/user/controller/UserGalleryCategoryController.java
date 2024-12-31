package com.juny.finalboard.domain.post.gallery.user.controller;

import com.juny.finalboard.domain.post.gallery.common.entity.GalleryCategory;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserGalleryCategoryController {

  private final GalleryCategoryService galleryCategoryService;

  @GetMapping("/v1/gallery-categories")
  public ResponseEntity<List<GalleryCategory>> getAllCategories() {

    List<GalleryCategory> categories = galleryCategoryService.getAllCategories();

    return new ResponseEntity<>(categories, HttpStatus.OK);
  }
}
