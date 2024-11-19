package com.juny.jspboardwithmybatis.domain.board.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResBoardDetail {
  private Long id;
  private String title;
  private String content;
  private int viewCount;
  private String createdAt;
  private String createdBy;
  private String updatedAt;
  private String categoryName;
  private List<ResBoardImage> boardImages;
  private List<ResAttachment> attachments;
  private List<ResComment> comments;
}
