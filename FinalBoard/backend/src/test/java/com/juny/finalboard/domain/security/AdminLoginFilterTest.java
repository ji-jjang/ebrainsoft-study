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
public class AdminLoginFilterTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("관리자 올바른 정보로 로그인 한다면, 성공한다")
  void adminLoginFilterSuccess() throws Exception {

    mockMvc
        .perform(
            post("/admin/v1/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "junyhehe@gmail.com")
                .param("password", "1234"))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  @DisplayName("관리자 비밀번호가 틀리다면, 인증 실패한다")
  void adminLoginFilterTest() throws Exception {

    mockMvc
        .perform(
            post("/admin/v1/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "junyhehe@gmail.com")
                .param("password", "12345"))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.msg").value("Bad credentials"));
  }
}
