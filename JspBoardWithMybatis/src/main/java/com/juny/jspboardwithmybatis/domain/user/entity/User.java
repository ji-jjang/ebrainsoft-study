package com.juny.jspboardwithmybatis.domain.user.entity;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class User {
  private Long id;
  private String name;
  private String email;
}
