package com.juny.finalboard.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.repository.AnnouncementPostRepository;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.domain.user.common.UserRepository;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserQueryTest {

  @Autowired private UserRepository userRepository;
  @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired
  private AnnouncementPostRepository announcementPostRepository;

  @Test
  @DisplayName("findById")
  @Disabled
  void findById() {

    User user = userRepository.findById(1L)
      .orElseThrow(() -> new RuntimeException("invalid user id"));

    assertThat(user.getId()).isEqualTo(1L);
    assertThat(user.getName()).isNotNull();
    assertThat(user.getEmail()).isNotNull();
    assertThat(user.getPassword()).isNotNull();
    assertThat(user.getRole()).isNotNull();
    assertThat(user.getCreatedAt()).isNotNull();
  }

  @Test
  @DisplayName("save")
  @Disabled
  void save() {

    User user = User.builder()
      .email("email@email.com")
      .password(bCryptPasswordEncoder.encode("1234"))
      .name("juny")
      .role("USER")
      .createdAt(LocalDateTime.now())
      .build();
    userRepository.save(user);
  }

  @Test
  @Disabled
  @DisplayName("increaseViewCount")
  void increaseViewCount() {

    AnnouncementPost post = announcementPostRepository
      .findPostDetailById(1L)
      .orElseThrow(() -> new RuntimeException("invalid post id"));

    announcementPostRepository.increaseViewCount(1L);

    AnnouncementPost post2 = announcementPostRepository
      .findPostDetailById(1L)
      .orElseThrow(() -> new RuntimeException("invalid post id"));

    Assertions.assertThat(post.getViewCount()).isEqualTo(post2.getViewCount() - 1);
  }
}
