package com.juny.jspboardwithmybatis.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResAttachment {

  private Long id;
  private String logicalName;
  private String storedName;
  private String storedPath;
  private String extension;
  private Integer size;
}
