package com.juny.finalboard.domain.post.question.common.entity;

import com.juny.finalboard.domain.user.common.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Answer {

  String content;
  private Long id;
  private QuestionPost post;
  private User user;
}
