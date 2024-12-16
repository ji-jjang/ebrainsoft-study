package com.juny.finalboard.domain.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
  @DisplayName("관리자로 로그인 성공했을 경우 인증 성공 후 /admin/home 경로로 리다이렉트 된다.")
  void adminLoginFilterTest() throws Exception {

    mockMvc
        .perform(
            post("/admin/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "junyhehe@gmail.com")
                .param("password", "1234"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/home"));
  }
}
