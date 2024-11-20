package com.juny.jspboardwithmybatis.domain.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtils {

  private static DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static DateTimeFormatter detailFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static String toSearchFormat(LocalDate startDate, String hourMinuteSecond) {
    return simpleFormatter.format(startDate) + " " + hourMinuteSecond;
  }

  public static String toOutputFormat(String dateTime) {

    LocalDate localDate;

    if (dateTime.split(" ").length == 2) {
      LocalDateTime localDateTime = LocalDateTime.parse(dateTime, detailFormatter);
      localDate = localDateTime.toLocalDate();
    } else {
      localDate = LocalDate.parse(dateTime, simpleFormatter);
    }

    return simpleFormatter.format(localDate);
  }
}
