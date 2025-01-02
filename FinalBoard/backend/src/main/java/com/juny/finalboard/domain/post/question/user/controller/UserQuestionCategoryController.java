package com.juny.finalboard.domain.post.question.user.controller;

import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import com.juny.finalboard.domain.post.question.common.service.QuestionCategoryService;
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
public class UserQuestionCategoryController {

  private final QuestionCategoryService questionCategoryService;

  /**
   *
   *
   * <h1>카테고리 조회 컨트롤러 </h1>
   *
   * @return categories
   */
  @GetMapping("/v1/question-categories")
  public ResponseEntity<List<QuestionCategory>> getAllCategories() {

    List<QuestionCategory> categories = questionCategoryService.getAllCategories();

    return new ResponseEntity<>(categories, HttpStatus.OK);
  }
}
