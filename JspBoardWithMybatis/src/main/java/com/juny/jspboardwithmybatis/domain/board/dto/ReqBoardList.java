package com.juny.jspboardwithmybatis.domain.board.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReqBoardList {

  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String categoryName;
  private final String keyword;
  private final int page;

  /**
   * <h1> AllArgsConstructor, 불변 객체로 만들 때 기본값 설정</h1>
   * @param startDate
   * @param endDate
   * @param categoryName
   * @param keyword
   * @param page
   */
  public ReqBoardList(
      LocalDate startDate, LocalDate endDate, String categoryName, String keyword, int page) {

    this.startDate = (startDate == null) ? LocalDate.now().minusYears(1) : startDate;
    this.endDate = (endDate == null) ? LocalDate.now() : endDate;
    this.categoryName = categoryName;
    this.keyword = keyword;
    this.page = page;
  }
}
