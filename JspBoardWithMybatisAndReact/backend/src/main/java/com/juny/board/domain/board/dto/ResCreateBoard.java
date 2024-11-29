package com.juny.board.domain.board.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ResCreateBoard {

  private final Long boardId;
  private final List<String> imageStoredNames;
  private final List<String> attachmentStoredNames;
}
