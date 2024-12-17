package com.juny.finalboard.domain.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginFilterTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("유저가 로그인 성공하면 액세스 토큰을 발급받는다")
  void loginSuccess() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "user@gmail.com")
                .param("password", "1234"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.accessTokenExpired").exists());
  }

  @Test
  @DisplayName("유저가 비밀번호 틀리면 로그인 실패한다")
  void loginFailedByPasswordNotMatch() throws Exception {

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "user@gmail.com")
                .param("password", "12345"))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.msg").value("Bad credentials"))
        .andExpect(jsonPath("$.accessToken").doesNotExist())
        .andExpect(jsonPath("$.accessTokenExpired").doesNotExist());
  }
}
