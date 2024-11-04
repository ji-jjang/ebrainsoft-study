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
}
