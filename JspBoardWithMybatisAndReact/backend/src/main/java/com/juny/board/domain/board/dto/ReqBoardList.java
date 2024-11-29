package com.juny.board.domain.board.dto;

import com.juny.board.global.Constants;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReqBoardList {

  private LocalDate startDate;
  private LocalDate endDate;
  private String categoryName;
  private String keyword;
  private Integer page;

  public ReqBoardList(
      LocalDate startDate, LocalDate endDate, String categoryName, String keyword, Integer page) {
    this.startDate = (startDate == null) ? LocalDate.now().minusYears(1) : startDate;
    this.endDate = (endDate == null) ? LocalDate.now() : endDate;
    this.categoryName = categoryName;
    this.keyword = keyword;
    this.page = (page == null || page <= 0) ? Constants.DEFAULT_PAGE_NUMBER : page;
  }
}
