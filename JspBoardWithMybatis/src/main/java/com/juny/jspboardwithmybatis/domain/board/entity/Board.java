package com.juny.jspboardwithmybatis.domain.board.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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

  /**
   * PRIMARY KEY 제외 생성자
   * @param title
   * @param content
   * @param password
   * @param viewCount
   * @param createdAt
   * @param createdBy
   * @param updatedAt
   * @param categoryId
   */
  public Board(
      String title,
      String content,
      String password,
      Integer viewCount,
      LocalDateTime createdAt,
      String createdBy,
      LocalDateTime updatedAt,
      Long categoryId) {
    this.title = title;
    this.content = content;
    this.password = password;
    this.viewCount = viewCount;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.updatedAt = updatedAt;
    this.categoryId = categoryId;
  }
}
