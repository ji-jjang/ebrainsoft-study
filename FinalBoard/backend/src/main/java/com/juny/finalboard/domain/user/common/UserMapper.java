package com.juny.finalboard.domain.user.common;

public class UserMapper {

  public static ResUser toResUser(User user) {

    return ResUser.builder()
        .id(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .role(user.getRole())
        .createdAt(user.getCreatedAt().toString())
        .build();
  }
}
