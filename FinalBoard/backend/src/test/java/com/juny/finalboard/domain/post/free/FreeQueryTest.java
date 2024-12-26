package com.juny.finalboard.domain.post.free;

import com.juny.finalboard.domain.post.free.common.dto.FreeSearchCondition;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import com.juny.finalboard.domain.post.free.common.entity.FreePostCategory;
import com.juny.finalboard.domain.post.free.common.repository.FreePostCategoryRepository;
import com.juny.finalboard.domain.post.free.common.repository.FreePostRepository;
import com.juny.finalboard.domain.post.free.common.service.FreePostService;
import com.juny.finalboard.domain.user.common.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FreeQueryTest {

  @Autowired private FreePostRepository freePostRepository;

  @Autowired private FreePostCategoryRepository freePostCategoryRepository;

  @Autowired private FreePostService freePostService;

  @Test
  @DisplayName("save")
  @Disabled
  void save() {

    for (int i = 1; i < 300; ++i) {
      FreePost freePost =
          FreePost.builder()
              .title("제목 " + i)
              .content("내용 " + i)
              .createdBy("작성자 " + i)
              .createdAt(LocalDateTime.now())
              .freePostCategory(FreePostCategory.builder().id(1L).build())
              .user(User.builder().id(3L).build())
              .build();

      freePostRepository.save(freePost);
    }
  }

  @Test
  @DisplayName("findPostDetailById")
  @Disabled
  void findPostDetailById() {

    FreePost freePost =
        freePostRepository.findFreePostDetailById(1L).orElseThrow(() -> new RuntimeException());

    System.out.println("freePost = " + freePost);
  }

  @Test
  @DisplayName("getTotalFreePostCount")
  @Disabled
  void getTotalFreePostCount() {

    FreeSearchCondition searchCondition =
        FreeSearchCondition.builder()
            .startDate(LocalDateTime.of(2024, 12, 15, 0, 0, 0).toString())
            .endDate(LocalDateTime.of(2024, 12, 31, 0, 0, 0).toString())
            .categoryId("1")
            .sort("created_at asc")
            .page(1)
            .pageSize(5)
            .build();

    long totalAnnouncementPostCount = freePostRepository.getTotalFreePostCount(searchCondition, -1);

    System.out.println("totalAnnouncementPostCount = " + totalAnnouncementPostCount);
  }

  @Test
  @DisplayName("findAllWithBySearchCondition")
  @Disabled
  void findAllWithBySearchCondition() {

    FreeSearchCondition searchCondition =
        FreeSearchCondition.builder()
            .startDate(LocalDateTime.of(2024, 12, 15, 0, 0, 0).toString())
            .endDate(LocalDateTime.of(2024, 12, 31, 0, 0, 0).toString())
            .categoryId("1")
            .sort("created_at asc")
            .page(1)
            .pageSize(5)
            .build();

    List<FreePost> freePostList =
        freePostRepository.findAllWithBySearchCondition(searchCondition, 0);

    for (var e : freePostList) {
      System.out.println("e.getId() = " + e.getId());
    }
  }

  @Test
  @DisplayName("findAllCategories")
  @Disabled
  void findAllCategories() {
    List<FreePostCategory> categoryList = freePostCategoryRepository.findAll();

    for (var e : categoryList) {
      System.out.println("e.getId() = " + e.getId());
      System.out.println("e.getName() = " + e.getName());
    }
  }
}
