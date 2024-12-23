package com.juny.finalboard.domain.user.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  private Long id;
  private String email;
  private String password;
  private String name;
  private String role;
  private LocalDateTime createdAt;
}
