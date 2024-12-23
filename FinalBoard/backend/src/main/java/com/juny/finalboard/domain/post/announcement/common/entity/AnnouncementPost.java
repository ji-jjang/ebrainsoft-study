package com.juny.finalboard.domain.post.announcement.common.entity;

import com.juny.finalboard.domain.user.user.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AnnouncementPost {

  private Long id;

  private String title;

  private String content;

  private Integer viewCount;

  private Boolean isPinned;

  private String createdBy;

  private LocalDateTime createdAt;

  private User user;

  private AnnouncementCategory announcementCategory;
}
