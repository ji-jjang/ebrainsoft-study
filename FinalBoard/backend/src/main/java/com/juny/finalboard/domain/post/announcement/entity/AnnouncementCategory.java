package com.juny.finalboard.domain.post.announcement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementCategory {

  private Long id;
  private String name;
}
