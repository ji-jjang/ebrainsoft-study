package com.juny.board.domain.board.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoVO {

  private int page;
  private int totalPages;
  private long totalBoardCount;
}
