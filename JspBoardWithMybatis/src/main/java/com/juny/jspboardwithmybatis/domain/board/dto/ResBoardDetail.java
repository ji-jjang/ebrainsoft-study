package com.juny.jspboardwithmybatis.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Setter
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

  public ResBoardDetail(
      @JsonProperty("id") Long id,
      @JsonProperty("title") String title,
      @JsonProperty("content") String content,
      @JsonProperty("viewCount") int viewCount,
      @JsonProperty("createdAt") String createdAt,
      @JsonProperty("createdBy") String createdBy,
      @JsonProperty("updatedAt") String updatedAt,
      @JsonProperty("categoryName") String categoryName,
      @JsonProperty("boardImages") List<ResBoardImage> boardImages,
      @JsonProperty("attachments") List<ResAttachment> attachments,
      @JsonProperty("comments") List<ResComment> comments) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.viewCount = viewCount;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.updatedAt = updatedAt;
    this.categoryName = categoryName;
    this.boardImages = boardImages;
    this.attachments = attachments;
    this.comments = comments;
  }
}
