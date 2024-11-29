package com.juny.board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ResComment {

  private Long id;
  private String content;
  private String createdAt;
  private String createdBy;
}
