package com.juny.board.domain.board.controller;

import com.juny.board.domain.board.dto.ResCategoryNames;
import com.juny.board.domain.utils.CategoryMapperUtils;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CategoryController {

  /**
   *
   *
   * <h1>카테고리 목록 조회</h1>
   *
   * <br>
   * - categoryMapperUtils Memory 조회
   *
   * @return ResCategoryNames
   */
  @GetMapping("/v1/categories")
  public ResCategoryNames getCategories() {

    List<String> categoryNames = CategoryMapperUtils.getAllCategoryName();

    return ResCategoryNames.builder().categoryNames(categoryNames).build();
  }
}
