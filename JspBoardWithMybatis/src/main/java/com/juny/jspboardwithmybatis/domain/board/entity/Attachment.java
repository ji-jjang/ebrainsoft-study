package com.juny.jspboardwithmybatis.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Attachment {

  private Long id;
  private String logicalName;
  private String storedName;
  private String storedPath;
  private String extension;
  private Long size;
  private Long boardId;

  /**
   * PRIMARY KEY 제외 생성자
   *
   * @param logicalName
   * @param storedName
   * @param storedPath
   * @param extension
   * @param size
   * @param boardId
   */
  public Attachment(
      String logicalName,
      String storedName,
      String storedPath,
      String extension,
      Long size,
      Long boardId) {
    this.logicalName = logicalName;
    this.storedName = storedName;
    this.storedPath = storedPath;
    this.extension = extension;
    this.size = size;
    this.boardId = boardId;
  }
}
