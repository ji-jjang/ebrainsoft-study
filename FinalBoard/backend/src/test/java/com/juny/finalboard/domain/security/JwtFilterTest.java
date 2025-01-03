package com.juny.finalboard.domain.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.juny.finalboard.global.constant.Constants;
import com.juny.finalboard.global.security.user.jwt.JwtUtil;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtFilterTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private JwtUtil jwtUtil;

  @Test
  @DisplayName("올바른 JWT 토큰으로 인증이 필요한 API 호출한다.")
  void apiCallWithValidJwt() throws Exception {

    String accessToken =
        jwtUtil.createJwt(
            1L,
            "USER",
            "juny",
            new Date(System.currentTimeMillis() + jwtUtil.getAccessTokenExpiredMileSecond()));

    mockMvc
        .perform(
            get("/api/v1/hello")
                .header(Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX + accessToken))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("만료 기한이 지난 JWT 토큰으로 인증이 필요한 API 호출하면 AT1 에러코드를 응답받는다")
  void apiCallWithExpiredJwt() throws Exception {

    LocalDate past = LocalDate.now().minusDays(1);

    String accessToken =
        jwtUtil.createJwt(
            1L, "USER", "juny", Date.from(past.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    mockMvc
        .perform(
            get("/api/v1/hello")
                .header(Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX + accessToken))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.msg").value("access token expired"))
        .andExpect(jsonPath("$.code").value("AT1"));
  }

  @Test
  @DisplayName("유효하지 않은 JWT 토큰으로 인증이 필요한 API 호출하면 AT2 에러코드를 응답받는다")
  void apiCallWithInvalidJwt() throws Exception {

    mockMvc
        .perform(
            get("/api/v1/hello")
                .header(Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX + "token abcd"))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.msg").value("access token is invalid"))
        .andExpect(jsonPath("$.code").value("AT2"));
  }
}
