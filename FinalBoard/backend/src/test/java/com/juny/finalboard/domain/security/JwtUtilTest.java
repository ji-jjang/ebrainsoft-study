package com.juny.finalboard.domain.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.juny.finalboard.global.security.user.jwt.JwtUtil;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {

  @Autowired private JwtUtil jwtUtil;

  @Test
  @DisplayName("Jwt 토큰을 생성하고, 아이디, 만료기간, 사용자 권한을 조회할 수 있는지 검사한다")
  void JwtVerify() {

    Long id = 1L;
    Date expiredDate =
        Date.from(
            Instant.now()
                .plusMillis(jwtUtil.getAccessTokenExpiredMileSecond())
                .truncatedTo(ChronoUnit.SECONDS));

    String role = "USER";

    String accessToken = jwtUtil.createJwt(id, role, expiredDate);

    assertThat(accessToken).isNotNull();

    assertThat(jwtUtil.getId(accessToken)).isEqualTo(id);

    assertThat(jwtUtil.getExpiration(accessToken)).isEqualTo(expiredDate);

    assertThat(jwtUtil.getRole(accessToken)).isEqualTo(role);
  }
}
