package com.juny.jspboardwithmybatis.domain.board.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResBoardDetail {
  private final Long id;
  private final String title;
  private final String content;
  private final int viewCount;
  private final String createdAt;
  private final String createdBy;
  private final String updatedAt;
  private final String categoryName;
  private final List<ResBoardImage> boardImages;
  private final List<ResAttachment> attachments;
  private final List<ResComment> comments;
}
