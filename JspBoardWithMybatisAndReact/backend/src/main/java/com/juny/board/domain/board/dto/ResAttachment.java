package com.juny.board.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResAttachment {

  private Long id;
  private String logicalName;
  private String storedName;
  private String storedPath;
  private String extension;
  private Integer size;
}
