package com.juny.finalboard.domain.post.question.common.service;

import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import com.juny.finalboard.domain.post.question.common.repository.QuestionCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService {

  private final QuestionCategoryRepository questionCategoryRepository;

  /**
   *
   *
   * <h1>질문 카테고리 조회 </h1>
   *
   * @return 질문 카테고리
   */
  public List<QuestionCategory> getAllCategories() {

    return questionCategoryRepository.findAll();
  }
}
