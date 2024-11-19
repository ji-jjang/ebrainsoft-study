package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResBoardImage {

  private Long id;
  private String storedName;
  private String storedPath;
  private String extension;
}
