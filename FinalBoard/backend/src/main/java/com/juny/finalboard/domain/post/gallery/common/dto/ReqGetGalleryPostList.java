package com.juny.finalboard.domain.post.gallery.common.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ReqGetGalleryPostList(
    String startDate,
    String endDate,
    String categoryId,
    String keyword,
    String sort,
    Integer page,
    Integer pageSize) {

  public ReqGetGalleryPostList {
    if (startDate == null || startDate.isEmpty()) {
      startDate = LocalDate.now().minusYears(1).toString();
    }
    if (endDate == null || endDate.isEmpty()) {
      endDate = LocalDate.now().toString();
    }
    if (keyword == null) {
      keyword = "";
    }
    if (categoryId == null) {
      categoryId = "";
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
