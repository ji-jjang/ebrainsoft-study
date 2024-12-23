package com.juny.finalboard.domain.user;

import static org.assertj.core.api.Assertions.*;

import com.juny.finalboard.domain.user.user.User;
import com.juny.finalboard.domain.user.user.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UserServiceTest {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("mybatis user save() 쿼리 테스트")
  @Disabled
  public void saveTest() {
    User user1 =
        User.builder()
            .email("admin@gmail.com")
            .name("어드민이름")
            .role("ADMIN")
            .password(passwordEncoder.encode("1234"))
            .createdAt(LocalDateTime.now())
            .build();

    User user2 =
        User.builder()
            .email("user@gmail.com")
            .name("유저이름")
            .role("USER")
            .password(passwordEncoder.encode("1234"))
            .createdAt(LocalDateTime.now())
            .build();

    userRepository.save(user1);
    userRepository.save(user2);
  }

  @Test
  @DisplayName("mybatis user findByEmail() 쿼리 테스트")
  public void getTest() {

    User user =
        userRepository
            .findByEmail("junyhehe@gmail.com")
            .orElseThrow(() -> new RuntimeException("user not found"));

    assertThat(user.getEmail()).isEqualTo("junyhehe@gmail.com");
  }
}
