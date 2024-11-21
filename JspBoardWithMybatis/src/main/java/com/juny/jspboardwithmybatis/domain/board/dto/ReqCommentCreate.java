package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ReqCommentCreate {

  private final String content;
  private final String password;
  private final String createdBy;
}
