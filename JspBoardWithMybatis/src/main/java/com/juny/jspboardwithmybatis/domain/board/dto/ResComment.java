package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResComment {

  private Long id;
  private String content;
  private String password;
  private String createdAt;
  private String createdBy;
}
