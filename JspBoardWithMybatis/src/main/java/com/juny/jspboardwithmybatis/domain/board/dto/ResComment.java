package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ResComment {

  private Long id;
  private String content;
  private String createdAt;
  private String createdBy;
}
