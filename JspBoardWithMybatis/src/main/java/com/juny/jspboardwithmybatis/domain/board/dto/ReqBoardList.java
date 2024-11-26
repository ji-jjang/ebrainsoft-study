package com.juny.jspboardwithmybatis.domain.board.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReqBoardList {

  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String categoryName;
  private final String keyword;
  private final Integer page;
}
