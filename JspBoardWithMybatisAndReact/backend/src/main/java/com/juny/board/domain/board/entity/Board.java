package com.juny.board.domain.board.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 *
 * <h1>엔티티에 없는 필드 </h1>
 *
 * - inputPassword: 사용자가 입력한 비밀번호<br>
 * - passwordConfirm: 비밀번호 확인<br>
 * - hasAttachment: 첨부파일 여부
 */
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Board {

  private Long id;
  private String title;
  private String content;
  private String inputPassword;
  private String password;
  private String passwordConfirm;
  private Integer viewCount;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime updatedAt;
  private Long categoryId;

  private Boolean hasAttachment;

  private Category category;

  private List<BoardImage> boardImages;
  private List<Attachment> attachments;
  private List<Comment> comments;
}
