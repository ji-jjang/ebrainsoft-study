package com.juny.jspboardwithmybatis.domain.board.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ReqBoardPreList {

  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String categoryName;
  private final String keyword;
  private final Integer page;
}
