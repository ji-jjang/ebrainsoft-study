package com.juny.board.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResBoardImage {

  private final Long id;
  private final String storedName;
  private final String storedPath;
  private final String extension;
}
