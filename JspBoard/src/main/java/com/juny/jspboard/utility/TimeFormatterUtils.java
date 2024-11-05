package com.juny.jspboard.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * jsp에서 시간을 출력하기 위한 클래스
 */
public class TimeFormatterUtils {

  private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * LocalDateTime을 String으로 변환하여 JSP에서 쉽게 출력하기 위함
   *
   * @param localDateTime the local date time
   * @return the string
   */
  public static String datetimeToString(LocalDateTime localDateTime) {

    if (localDateTime == null) {
      return "-";
    }
    return localDateTime.format(formatter);
  }

  /**
   * 현재 시간 기준 1년전 기준으로 기본 시작일 등록
   *
   * @return yyyy-mm-dd
   */
  public static String getDefaultStartDate() {
    return LocalDate.now().minusYears(1).format(dateFormatter);
  }

  /**
   * 현재 시간 기준 기본 종료일 등록
   *
   * @return yyyy-mm-dd
   */
  public static String getDefaultEndDate() {
    return LocalDate.now().format(dateFormatter);
  }

  /**
   * 유틸리티 정적 클래스 인스턴스 방지
   */
  private TimeFormatterUtils() {
  }
}
