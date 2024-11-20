package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResSearchCondition {

  private String startDate;
  private String endDate;
  private String keyword;
  private String categoryName;
}
