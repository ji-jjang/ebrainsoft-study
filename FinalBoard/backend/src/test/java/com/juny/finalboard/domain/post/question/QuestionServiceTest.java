package com.juny.finalboard.domain.post.question;

import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPostList;
import com.juny.finalboard.domain.post.question.common.dto.ReqUpdateQuestionPost;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.service.QuestionService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionServiceTest {

  @Autowired
  private QuestionService questionService;

  @Test
  @DisplayName("createQuestion")
  @Disabled
  void createQuestion() {

    for (int i = 2; i < 110; ++i) {

      ReqCreateQuestionPost req = ReqCreateQuestionPost.builder()
        .title("title " + i)
        .content("content " + i)
        .categoryId("2")
        .isSecret(false)
        .build();

      QuestionPost questionPost = questionService.createQuestionPost(req, 3L);
    }
  }

  @Test
  @DisplayName("getQuestionPostDetail")
  @Disabled
  void getQuestionPostDetail() {

    QuestionPost post = questionService.getQuestionPostDetail(1L);
  }

  @Test
  @DisplayName("increaseViewCount")
  @Disabled
  void increaseViewCount() {

    questionService.increaseViewCount(1L);
  }

  @Test
  @DisplayName("getQuestionPostListBySearchCondition")
  @Disabled
  void getQuestionPostListBySearchCondition() {

    ReqGetQuestionPostList req = ReqGetQuestionPostList.builder()
      .startDate(LocalDate.of(2024, 12, 1).toString())
      .endDate(LocalDate.of(2025, 1, 31).toString())
      .categoryId("2")
      .keyword("")
      .sort("")
      .page(11)
      .pageSize(10)
      .build();

    QuestionSearchCondition searchCondition = questionService.createSearchCondition(req);

    long totalPostCountBySearchCondition = questionService.getTotalPostCountBySearchCondition(
      searchCondition);

    List<QuestionPost> postList = questionService.getQuestionPostListBySearchCondition(
      searchCondition);
  }

  @Test
  @DisplayName("updatePost")
  @Disabled
  void updatePost() {

    QuestionPost post = questionService.getQuestionPostDetail(1L);
    ReqUpdateQuestionPost req = ReqUpdateQuestionPost.builder()
      .title("update title")
      .content("update content")
      .categoryId("2")
      .isSecret(false)
      .build();

    questionService.updatePost(req, post, 3L);
  }

  @Test
  @DisplayName("deletePost")
  @Disabled
  void deletePost() {
    QuestionPost post = questionService.getQuestionPostDetail(1L);

    questionService.deletePost(1L, post, 3L);
  }
}
