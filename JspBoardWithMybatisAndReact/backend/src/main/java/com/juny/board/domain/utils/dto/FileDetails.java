package com.juny.board.domain.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class FileDetails {

  private final String logicalName;
  private final String storedName;
  private final String storedPath;
  private final String extension;
  private final Integer size;
}
