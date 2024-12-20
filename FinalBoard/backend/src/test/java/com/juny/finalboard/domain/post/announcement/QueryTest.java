package com.juny.finalboard.domain.post.announcement;

import com.juny.finalboard.domain.post.announcement.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.entity.AnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.entity.AnnouncementPost;
import com.juny.finalboard.domain.post.announcement.repository.AnnouncementCategoryRepository;
import com.juny.finalboard.domain.post.announcement.repository.AnnouncementPostRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class QueryTest {

  @Autowired private AnnouncementCategoryRepository announcementCategoryRepository;

  @Autowired private AnnouncementPostRepository announcementPostRepository;

  @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Test
  @DisplayName("findAllCategories")
  @Disabled
  void findAllCategories() {

    List<AnnouncementCategory> categories = announcementCategoryRepository.findAll();

    for (var e : categories) {
      System.out.println("e.getName() = " + e.getName());
    }
  }

  @Test
  @DisplayName("saveAnnouncementPost")
  @Disabled
  void saveAnnouncementPost() {

    AnnouncementCategory category1 =
        announcementCategoryRepository
            .findById(1L)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    for (int i = 1; i < 10; ++i) {
      AnnouncementPost announcementPost =
          AnnouncementPost.builder()
              .title("제목 " + i)
              .content("내용 " + i)
              .password(bCryptPasswordEncoder.encode("1234"))
              .viewCount(0)
              .isPinned(true)
              .createdAt(LocalDateTime.now())
              .createdBy("작성자 " + i)
              .announcementCategory(category1)
              .build();

      announcementPostRepository.save(announcementPost);
    }
  }

  @Test
  @DisplayName("findCategoryByCategoryId")
  @Disabled
  void getCategoryById() {

    AnnouncementCategory category =
        announcementCategoryRepository
            .findById(1L)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    Assertions.assertThat(category.getName()).isEqualTo("공지");
  }

  @Test
  @DisplayName("getTotalAnnouncementPostCount")
  @Disabled
  void getTotalAnnouncementPostCount() {

    SearchCondition searchCondition =
        SearchCondition.builder()
            .startDate(LocalDateTime.of(2024, 12, 15, 0, 0, 0).toString())
            .endDate(LocalDateTime.of(2024, 12, 31, 0, 0, 0).toString())
            .categoryName("공지")
            .sort("created_at asc")
            .page(1)
            .pageSize(5)
            .offset(0)
            .build();

    long totalBoardCount =
        announcementPostRepository.getTotalAnnouncementPostCount(searchCondition);

    System.out.println("totalBoardCount = " + totalBoardCount);
  }

  @Test
  @DisplayName("findAllWithCategoryBySearchCondition")
  @Disabled
  void findAllWithCategoryBySearchCondition() {

    SearchCondition searchCondition =
        SearchCondition.builder()
            .startDate(LocalDateTime.of(2024, 12, 15, 0, 0, 0).toString())
            .endDate(LocalDateTime.of(2024, 12, 31, 0, 0, 0).toString())
            .categoryName("공지")
            .sort("created_at asc")
            .page(1)
            .pageSize(5)
            .offset(0)
            .build();

    List<AnnouncementPost> postList =
        announcementPostRepository.findAllWithCategoryBySearchCondition(searchCondition);

    for (var e : postList) {
      System.out.println("e.getId() = " + e.getId());
    }
  }

  @Test
  @DisplayName("findPostDetailById")
  @Disabled
  void findPostDetailById() {

    AnnouncementPost post =
        announcementPostRepository.findPostDetailById(7L).orElseThrow(RuntimeException::new);
  }

  @Test
  @DisplayName("updatePost")
  @Disabled
  void updatePost() {

    AnnouncementPost post =
        AnnouncementPost.builder()
            .id(2L)
            .title("변경된 제목")
            .content("변경된 내용")
            .isPinned(true)
            .createdBy("변경된 작성자")
            .announcementCategory(
                announcementCategoryRepository
                    .findById(2L)
                    .orElseThrow(() -> new RuntimeException()))
            .build();

    announcementPostRepository.updatePost(post);
  }

  @Test
  @DisplayName("deleteAnnouncementPostById")
  @Disabled
  void deleteAnnouncementPostById() {

    announcementPostRepository.deleteAnnouncementPostById(1L);
  }
}
