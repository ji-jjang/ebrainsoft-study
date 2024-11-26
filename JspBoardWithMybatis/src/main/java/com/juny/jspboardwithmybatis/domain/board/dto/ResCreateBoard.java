package com.juny.jspboardwithmybatis.domain.board.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResCreateBoard {

  private final Long boardId;
  private final List<String> imageStoredNames;
  private final List<String> attachmentStoredNames;
}
