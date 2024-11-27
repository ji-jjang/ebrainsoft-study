package com.juny.board.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResComment {

  private final Long id;
  private final String content;
  private final String createdAt;
  private final String createdBy;
}
