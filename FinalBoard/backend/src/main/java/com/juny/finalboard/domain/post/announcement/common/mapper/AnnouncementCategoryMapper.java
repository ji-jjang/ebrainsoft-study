package com.juny.finalboard.domain.post.announcement.common.mapper;

import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementCategoryList;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementCategory;
import java.util.List;

public class AnnouncementCategoryMapper {

  public static ResAnnouncementCategory toResAnnouncementCategory(
      AnnouncementCategory announcementCategory) {

    return ResAnnouncementCategory.builder()
        .id(announcementCategory.getId())
        .name(announcementCategory.getName())
        .build();
  }

  public static ResAnnouncementCategoryList toResAnnouncementCategoryList(
      List<AnnouncementCategory> announcementCategoryList) {

    List<ResAnnouncementCategory> resCategoryList =
        announcementCategoryList.stream()
            .map(AnnouncementCategoryMapper::toResAnnouncementCategory)
            .toList();

    return new ResAnnouncementCategoryList(resCategoryList);
  }
}
