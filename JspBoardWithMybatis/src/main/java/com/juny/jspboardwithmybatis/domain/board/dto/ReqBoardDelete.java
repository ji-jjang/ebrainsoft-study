package com.juny.jspboardwithmybatis.domain.board.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ReqBoardDelete {

  private final Long boardId;
  private final String password;
  private final List<ResBoardImage> boardImages;
  private final List<ResAttachment> attachments;
  private final List<ResComment> comments;
  private final List<String> deleteFilePaths;
}
