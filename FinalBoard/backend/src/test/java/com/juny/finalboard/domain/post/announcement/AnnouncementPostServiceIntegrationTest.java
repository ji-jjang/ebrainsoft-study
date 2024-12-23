package com.juny.finalboard.domain.post.announcement;

import static org.assertj.core.api.Assertions.*;

import com.juny.finalboard.domain.post.announcement.admin.service.AdminAnnouncementPostService;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostCreate;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostUpdate;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.service.AnnouncementPostService;
import com.juny.finalboard.domain.user.user.User;
import com.juny.finalboard.domain.user.user.UserRepository;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AnnouncementPostServiceIntegrationTest {

  @Autowired private AnnouncementPostService announcementPostService;

  @Autowired private AdminAnnouncementPostService adminAnnouncementPostService;

  @Autowired private UserRepository userRepository;

  @Autowired private BCryptPasswordEncoder encoder;

  @Test
  @DisplayName("게시판 CRUD 테스트")
  @Transactional
  void AnnouncementPostServiceCRUDTest() {

    User user =
        User.builder()
            .email("dummy@gmail.com")
            .password(encoder.encode("1234"))
            .name("dummy")
            .role(Constants.USER_ROLE)
            .createdAt(LocalDateTime.now())
            .build();

    userRepository.save(user);

    ReqPostCreate reqPostCreate =
        ReqPostCreate.builder()
            .title("제목 1")
            .content("내용 1")
            .isPinned(false)
            .categoryId(1L)
            .build();

    AnnouncementPost post = adminAnnouncementPostService.createPost(reqPostCreate, user.getId());

    assertThat(post).isNotNull();

    assertThat(post.getCreatedBy()).isEqualTo(user.getName());

    assertThat(post.getIsPinned()).isEqualTo(false);

    AnnouncementPost getPost = announcementPostService.getPostById(post.getId());

    assertThat(getPost).isNotNull();

    assertThat(getPost.getTitle()).isEqualTo(post.getTitle());

    ReqPostUpdate reqPostupdate =
        ReqPostUpdate.builder()
            .title("제목 변경1")
            .content("내용 변경1")
            .isPinned(true)
            .categoryId(2L)
            .build();

    AnnouncementPost updatedPost =
        adminAnnouncementPostService.updatePost(reqPostupdate, post.getId(), user.getId());

    assertThat(updatedPost).isNotNull();

    assertThat(updatedPost.getTitle()).isEqualTo(reqPostupdate.title());

    assertThat(updatedPost.getContent()).isEqualTo(reqPostupdate.content());

    assertThat(updatedPost.getIsPinned()).isEqualTo(reqPostupdate.isPinned());

    assertThat(updatedPost.getAnnouncementCategory().getId()).isEqualTo(2L);

    adminAnnouncementPostService.deleteAnnouncementPostById(post.getId());

    assertThatThrownBy(() -> announcementPostService.getPostById(post.getId()))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("invalid post id:");
  }
}
