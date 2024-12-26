package com.juny.finalboard.domain.post.free.common.entity;

import com.juny.finalboard.domain.user.common.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FreePost {

  private Long id;
  private String title;
  private String content;
  private Integer viewCount;
  private String createdBy;
  private LocalDateTime createdAt;

  private FreePostCategory freePostCategory;
  private User user;

  private List<FreeComment> freeCommentList;
  private List<FreeAttachment> freeAttachmentList;
}
