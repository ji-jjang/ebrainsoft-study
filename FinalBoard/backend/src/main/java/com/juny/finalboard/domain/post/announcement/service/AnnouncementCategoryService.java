package com.juny.finalboard.domain.post.announcement.service;

import com.juny.finalboard.domain.post.announcement.entity.AnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.repository.AnnouncementCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementCategoryService {

  private final AnnouncementCategoryRepository announcementCategoryRepository;

  /**
   *
   *
   * <h1>모든 카테고리 조회</h1>
   *
   * @return 모든 카테고리 목록
   */
  public List<AnnouncementCategory> getAllCategories() {

    return announcementCategoryRepository.findAll();
  }
}
