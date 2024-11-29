package com.juny.board.domain.board.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchConditionVO {

  private String startDate;
  private String endDate;
  private String categoryName;
  private String keyword;
  private Integer page;
  private Integer limit;
  private Integer offset;
}
