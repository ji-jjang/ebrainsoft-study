package com.juny.jspboard.board.entity;

import java.time.LocalDateTime;

public class Comment {

  private Long id;
  private String content;
  private String password;
  private LocalDateTime createdAt;
  private String createdBy;
  private Long boardId;

  public Comment(
      Long id,
      String content,
      String password,
      LocalDateTime createdAt,
      String createdBy,
      Long boardId) {
    this.id = id;
    this.content = content;
    this.password = password;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.boardId = boardId;
  }

  public Comment(
      String content, String password, LocalDateTime createdAt, String createdBy, Long boardId) {
    this.content = content;
    this.password = password;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.boardId = boardId;
  }

  public Long getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public String getContent() {
    return content;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public Long getBoardId() {
    return boardId;
  }
}
