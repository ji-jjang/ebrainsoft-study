package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResPageInfo {

  private int totalBoardCount;
  private int totalPages;
  private int page;
}
