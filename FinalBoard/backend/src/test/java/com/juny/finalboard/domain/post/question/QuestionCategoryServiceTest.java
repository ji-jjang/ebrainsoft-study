package com.juny.finalboard.domain.post.question;

import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import com.juny.finalboard.domain.post.question.common.service.QuestionCategoryService;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionCategoryServiceTest {

  @Autowired QuestionCategoryService questionCategoryService;

  @Test
  @DisplayName("getAllCategories")
  @Disabled
  void getAllCategories() {

    List<QuestionCategory> categories = questionCategoryService.getAllCategories();
  }
}
