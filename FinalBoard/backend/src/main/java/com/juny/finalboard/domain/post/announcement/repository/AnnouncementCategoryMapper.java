package com.juny.finalboard.domain.post.announcement.repository;

import com.juny.finalboard.domain.post.announcement.dto.ResAnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.dto.ResAnnouncementCategoryList;
import com.juny.finalboard.domain.post.announcement.entity.AnnouncementCategory;
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
