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
public class QuestionPost {

  private Long id;
  private String title;
  private String content;
  private Integer viewCount;
  private Boolean isSecret;
  private String password;
  private String createdBy;
  private LocalDateTime createdAt;

  private QuestionCategory questionCategory;
  private User user;

  private Answer answer;
}
