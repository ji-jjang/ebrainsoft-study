package com.juny.board.domain.board.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comment {

  private Long id;
  private String content;
  private String password;
  private LocalDateTime createdAt;
  private String createdBy;
  private Long boardId;

  /**
   * PRIMARY KEY 제외 생성자
   *
   * @param content
   * @param password
   * @param createdAt
   * @param createdBy
   * @param boardId
   */
  public Comment(
      String content, String password, LocalDateTime createdAt, String createdBy, Long boardId) {
    this.content = content;
    this.password = password;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.boardId = boardId;
  }
}
