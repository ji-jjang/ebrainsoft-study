package com.juny.finalboard.domain.post.free.common.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FreeComment {

  private Long id;
  private String content;
  private String createdBy;
  private LocalDateTime createdAt;

  private FreePost freePost;
}
