package com.juny.jspboardwithmybatis.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardImage {

  private Long id;
  private String storedName;
  private String storedPath;
  private String extension;
  private Long boardId;

  /**
   * PRIMARY KEY 제외 생성자
   *
   * @param storedName
   * @param storedPath
   * @param extension
   * @param boardId
   */
  public BoardImage(String storedName, String storedPath, String extension, Long boardId) {
    this.storedName = storedName;
    this.storedPath = storedPath;
    this.extension = extension;
    this.boardId = boardId;
  }
}
