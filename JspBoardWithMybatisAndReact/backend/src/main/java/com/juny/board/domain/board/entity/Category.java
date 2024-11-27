package com.juny.board.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Category {

  private Long id;
  private String name;

  /**
   * PRIMARY KEY 제외 생성자
   *
   * @param name
   */
  public Category(String name) {
    this.name = name;
  }
}
