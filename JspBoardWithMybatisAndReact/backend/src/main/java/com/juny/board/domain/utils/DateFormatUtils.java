package com.juny.board.domain.utils;

import com.juny.board.global.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatUtils {

  private static DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   *
   *
   * <h1>검색 조건 쿼리를 위해 시간분초를 붙이는 시간 형식 포매터</h1>
   *
   * <br>
   * - 검색 시작 시간에 00:00:00, 검색 종료 시간에 23:59:59 접미사 붙임
   *
   * @param startDate
   * @param hourMinuteSecond
   * @return
   */
  public static String toSearchFormat(LocalDate startDate, String hourMinuteSecond) {
    return simpleFormatter.format(startDate) + Constants.SPACE_SIGN + hourMinuteSecond;
  }

  /**
   *
   *
   * <h1>View 위한 시간 형식 포맷터</h1>
   *
   * <br>
   * - MySQL, DateFormat(String)으로 반환된 시간형식을 yyyy-MM-dd 로 변환<br>
   * - 시간분초 접미사가 붙는 경우가 존재
   *
   * @param dateFormat
   * @return
   */
  public static String toOutputFormat(String dateFormat) {

    String date = dateFormat;
    if (dateFormat.split(Constants.SPACE_SIGN).length == 2) {
      date = dateFormat.split(Constants.SPACE_SIGN)[0];
    }

    return date;
  }
}
