package com.juny.finalboard.domain.post.free.user.controller;

import com.juny.finalboard.domain.post.free.common.entity.FreePostCategory;
import com.juny.finalboard.domain.post.free.common.service.FreePostCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserFreePostCategoryController {

  private final FreePostCategoryService freePostCategoryService;

  /**
   *
   *
   * <h1>자유 게시판 카테고리 조회 </h1>
   *
   * @return List<FreePostCategory>
   */
  @GetMapping("/v1/free-categories")
  public ResponseEntity<List<FreePostCategory>> getAllCategories() {

    List<FreePostCategory> categoryList = freePostCategoryService.getAllCategories();

    return new ResponseEntity<>(categoryList, HttpStatus.OK);
  }
}
