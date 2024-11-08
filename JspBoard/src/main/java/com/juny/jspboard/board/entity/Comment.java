package com.juny.jspboard.board.entity;

import java.time.LocalDateTime;

public class Comment {

  private Long id;
  private String password;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime createdBy;

  public Comment(String password, String content, LocalDateTime createdBy) {
    this.password = password;
    this.content = content;
    this.createdBy = createdBy;
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

  public LocalDateTime getCreatedBy() {
    return createdBy;
  }
}
