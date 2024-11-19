package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResAttachment {

  private Long id;
  private String logicalName;
  private String storedName;
  private String storedPath;
  private String extension;
  private Integer size;
}
