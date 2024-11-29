package com.juny.board.domain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReqBoardDelete {

  @NotNull(message = "not null")
  @NotEmpty(message = "password not empty")
  @Size(min = 4, max = 15, message = "password min 4, max 15")
  private String password;
}
