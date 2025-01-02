package com.juny.finalboard.domain.post.question;

import com.juny.finalboard.domain.post.question.admin.service.AdminAnswerService;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateAnswer;
import com.juny.finalboard.domain.post.question.common.entity.Answer;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.repository.QuestionPostRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminAnswerServiceTest {

  @Autowired
  private AdminAnswerService adminAnswerService;

  @Autowired
  private QuestionPostRepository questionPostRepository;

  @Test
  @DisplayName("createAnswer")
  @Disabled
  void createAnswer() {

    QuestionPost post = questionPostRepository
      .findQuestionPostDetailById(1L)
      .orElseThrow(() -> new RuntimeException("invalid post Id"));


        ReqCreateAnswer req = ReqCreateAnswer.builder()
          .content("answer content 2")
          .build();

    Answer answer = adminAnswerService.createAnswer(req, post, 3L);
  }
}
