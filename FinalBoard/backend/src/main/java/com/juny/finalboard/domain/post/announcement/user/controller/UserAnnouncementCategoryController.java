package com.juny.finalboard.domain.post.announcement.user.controller;

import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementCategoryList;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.common.mapper.AnnouncementCategoryMapper;
import com.juny.finalboard.domain.post.announcement.common.service.AnnouncementCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserAnnouncementCategoryController {

  private final AnnouncementCategoryService announcementCategoryService;

  /**
   *
   *
   * <h1>모든 카테고리 조회 </h1>
   *
   * @return 모든 카테고리
   */
  @GetMapping("/v1/announcement-categories")
  public ResponseEntity<ResAnnouncementCategoryList> getAllCategories() {

    List<AnnouncementCategory> categoryList = announcementCategoryService.getAllCategories();

    ResAnnouncementCategoryList resAnnouncementCategoryList =
        AnnouncementCategoryMapper.toResAnnouncementCategoryList(categoryList);

    return new ResponseEntity<>(resAnnouncementCategoryList, HttpStatus.OK);
  }
}
