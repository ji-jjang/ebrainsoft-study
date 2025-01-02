package com.juny.finalboard.domain.post.question.common.entity;

import com.juny.finalboard.domain.user.common.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Answer {

  private Long id;
  private String content;
  private LocalDateTime createdAt;

  private QuestionPost post;
  private User user;
}
