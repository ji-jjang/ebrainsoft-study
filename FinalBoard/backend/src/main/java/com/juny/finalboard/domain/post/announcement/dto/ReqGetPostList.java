package com.juny.finalboard.domain.post.announcement.dto;

import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDate;

public record ReqGetPostList(
    String startDate,
    String endDate,
    String categoryName,
    String keyword,
    String sort,
    Integer page,
    Integer pageSize) {

  public ReqGetPostList {
    if (startDate == null || startDate.isEmpty()) {
      startDate = LocalDate.now().minusYears(1).toString();
    }
    if (endDate == null || endDate.isEmpty()) {
      endDate = LocalDate.now().toString();
    }
    if (categoryName == null) {
      categoryName = Constants.ANNOUNCEMENT_CATEGORY_DEFAULT_NAME;
    }
    if (keyword == null) {
      keyword = "";
    }
    if (sort == null || sort.isEmpty()) {
      sort = "created_at:desc";
    }
    if (page == null || page < 1) {
      page = 1;
    }
    if (pageSize == null || pageSize < 1) {
      pageSize = 10;
    }
  }
}
