package com.juny.finalboard.domain.post.announcement.common.mapper;

import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementCategoryList;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementCategory;
import java.util.List;

public class AnnouncementCategoryMapper {

  /**
   *
   *
   * <h1>카테고리 단건 매퍼 </h1>
   *
   * @param announcementCategory 카테고리 단건
   * @return ResAnnouncementCategory
   */
  public static ResAnnouncementCategory toResAnnouncementCategory(
      AnnouncementCategory announcementCategory) {

    return ResAnnouncementCategory.builder()
        .id(announcementCategory.getId())
        .name(announcementCategory.getName())
        .build();
  }

  /**
   *
   *
   * <h1>카테고리 목록 매퍼 </h1>
   *
   * @param announcementCategoryList 카테고리 목록
   * @return ResAnnouncementCategoryList
   */
  public static ResAnnouncementCategoryList toResAnnouncementCategoryList(
      List<AnnouncementCategory> announcementCategoryList) {

    List<ResAnnouncementCategory> resCategoryList =
        announcementCategoryList.stream()
            .map(AnnouncementCategoryMapper::toResAnnouncementCategory)
            .toList();

    return new ResAnnouncementCategoryList(resCategoryList);
  }
}
