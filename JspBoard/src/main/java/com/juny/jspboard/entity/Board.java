package com.juny.jspboard.entity;


import java.time.LocalDateTime;

public class Board {

  private Long id;
  private String title;
  private String content;
  private String password;
  private Integer viewCount;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime updatedAt;
  private Long categoryId;

  public Board(String title, String content, String password, Integer viewCount,
    LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, Long categoryId) {
    this.title = title;
    this.content = content;
    this.password = password;
    this.viewCount = viewCount;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.updatedAt = updatedAt;
    this.categoryId = categoryId;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getPassword() {
    return password;
  }

  public Integer getViewCount() {
    return viewCount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public Long getCategoryId() {
    return categoryId;
  }
}
