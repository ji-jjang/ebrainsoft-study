package com.juny.board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ResSearchCondition {

  private String startDate;
  private String endDate;
  private String keyword;
  private String categoryName;
}
