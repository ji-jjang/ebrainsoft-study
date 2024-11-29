package com.juny.jspboardwithmybatis.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ResBoardImage {


  private Long id;
  private String storedName;
  private String storedPath;
  private String extension;
}
