package com.juny.board.domain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 *
 * <h1>NoArgsConstructor 가 없으면 @RequestBody 직렬화 실패</h1>
 *
 * - 항상 NoArgsConstructor 추가하면 생기는 문제<br>
 * - ModelAttribute 매핑하여 기본 생성자로 값을 주는 경우, NoArgsConstructor 있으면 이를 먼저 사용<br>
 * - setter 를 열어두지 않고, 일관성을 확보할 방법?
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqBoardDelete {

  @NotNull(message = "not null")
  @NotEmpty(message = "password not empty")
  @Size(min = 4, max = 15, message = "password min 4, max 15")
  private String password;
}
