package com.juny.board.domain.board.entity;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Attachment implements FileInfo {

  private Long id;
  private String logicalName;
  private String storedName;
  private String storedPath;
  private String extension;
  private Integer size;
  private Long boardId;

  @Override
  public Optional<String> logicalName() {
    return Optional.ofNullable(logicalName);
  }

  @Override
  public String storedName() {
    return storedName;
  }

  @Override
  public String storedPath() {
    return storedPath;
  }

  @Override
  public Optional<String> extension() {
    return Optional.ofNullable(extension);
  }

  @Override
  public Optional<Integer> size() {
    return Optional.ofNullable(size);
  }
}
