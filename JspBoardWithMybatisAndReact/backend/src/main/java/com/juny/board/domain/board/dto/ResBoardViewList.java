package com.juny.board.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResBoardViewList {

  private final Long id;
  private final String title;
  private final Integer viewCount;
  private final String createdAt;
  private final String createdBy;
  private final String updatedAt;
  private final String categoryName;
  private final Boolean hasAttachment;
}
