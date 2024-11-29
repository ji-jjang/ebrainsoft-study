package com.juny.board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ResPageInfo {

  private int page;
  private int totalPages;
  private long totalBoardCount;
}
