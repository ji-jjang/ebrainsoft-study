package com.juny.board.domain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class ReqCommentCreate {

  @NotNull(message = "content not null")
  @NotEmpty(message = "content not empty")
  @Size(min = 4, max = 1999, message = "content min 4, max 1999")
  private String content;

  @NotNull(message = "password not null")
  @NotEmpty(message = "password not empty")
  @Size(min = 4, max = 15, message = "password min 4, max 15")
  private String password;

  @NotNull(message = "createdBy not null")
  @NotEmpty(message = "createdBy not empty")
  @Size(min = 3, max = 4, message = "createdBy min 3, max 4")
  private String createdBy;
}
