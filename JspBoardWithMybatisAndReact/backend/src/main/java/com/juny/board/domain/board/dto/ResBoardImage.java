package com.juny.board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResBoardImage {

  private Long id;
  private String storedName;
  private String storedPath;
  private String extension;
}
