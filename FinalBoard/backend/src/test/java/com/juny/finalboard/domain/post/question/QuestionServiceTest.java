package com.juny.finalboard.domain.post.question;

import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqDeleteQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPostList;
import com.juny.finalboard.domain.post.question.common.dto.ReqUpdateQuestionPost;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.service.QuestionService;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class QuestionServiceTest {

  @Autowired private QuestionService questionService;

  @Autowired private BCryptPasswordEncoder encoder;

  @Test
  @DisplayName("createQuestion")
  void createQuestion() {

    for (int i = 2; i < 110; ++i) {

      ReqCreateQuestionPost req =
          ReqCreateQuestionPost.builder()
              .title("title " + i)
              .content("content " + i)
              .password("1234")
              .passwordConfirm("1234")
              .categoryId("1")
              .isSecret(true)
              .build();

      QuestionPost questionPost = questionService.createQuestionPost(req, 3L);
    }
  }

  @Test
  @DisplayName("getQuestionPostDetail")
  @Disabled
  void getQuestionPostDetail() {

    CustomUserDetails userDetails =
        new CustomUserDetails(User.builder().id(3L).role("USER").build());

    ReqGetQuestionPost req = ReqGetQuestionPost.builder().password("1234").build();
    QuestionPost post = questionService.getQuestionPostDetail(2L, req, userDetails);
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

    ReqGetQuestionPostList req =
        ReqGetQuestionPostList.builder()
            .startDate(LocalDate.of(2024, 12, 1).toString())
            .endDate(LocalDate.of(2025, 1, 31).toString())
            .categoryId("2")
            .keyword("")
            .sort("")
            .page(11)
            .pageSize(10)
            .build();

    QuestionSearchCondition searchCondition = questionService.createSearchCondition(req);

    long totalPostCountBySearchCondition =
        questionService.getTotalPostCountBySearchCondition(searchCondition);

    List<QuestionPost> postList =
        questionService.getQuestionPostListBySearchCondition(searchCondition);
  }

  @Test
  @DisplayName("updatePost")
  @Disabled
  void updatePost() {

    CustomUserDetails userDetails =
        new CustomUserDetails(User.builder().id(3L).role("USER").build());

    ReqGetQuestionPost req = ReqGetQuestionPost.builder().password("1234").build();

    QuestionPost post = questionService.getQuestionPostDetail(1L, req, userDetails);
    ReqUpdateQuestionPost updateReq =
        ReqUpdateQuestionPost.builder()
            .title("update title")
            .content("update content")
            .categoryId("1")
            .password("1234")
            .isSecret(true)
            .build();

    questionService.updatePost(updateReq, post, userDetails);
  }

  @Test
  @DisplayName("deletePost")
  void deletePost() {

    Long postId = 1L;
    CustomUserDetails userDetails =
        new CustomUserDetails(User.builder().id(3L).role("USER").build());

    ReqDeleteQuestionPost req = ReqDeleteQuestionPost.builder().password("1234").build();

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().password("1234").build(), userDetails);

    questionService.deletePost(req, post, userDetails);
  }
}
