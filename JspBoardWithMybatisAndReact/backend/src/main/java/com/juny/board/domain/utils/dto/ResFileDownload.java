package com.juny.board.domain.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResFileDownload {

  private final String path;
  private final String logicalName;
}
