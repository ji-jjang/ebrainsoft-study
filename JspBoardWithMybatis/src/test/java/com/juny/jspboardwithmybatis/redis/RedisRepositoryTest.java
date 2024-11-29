package com.juny.jspboardwithmybatis.redis;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisRepositoryTest {

@Autowired
private RefreshTokenRepository refreshTokenRepository;

@Test
void saveRefreshToken() {

  RefreshToken refreshToken = new RefreshToken(1L, 1L, "asdkfaisxk291j28");

  RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

  RefreshToken findToken = refreshTokenRepository
    .findById(1L)
    .orElseThrow(() -> new RuntimeException("Refresh token not found"));

  assertThat(savedToken.getId()).isEqualTo(findToken.getId());
  assertThat(savedToken.getRefresh()).isEqualTo(findToken.getRefresh());
  assertThat(savedToken.getUserId()).isEqualTo(findToken.getUserId());
}

}
