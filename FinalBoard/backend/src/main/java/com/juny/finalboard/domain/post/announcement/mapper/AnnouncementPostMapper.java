package com.juny.finalboard.domain.post.announcement.mapper;

import com.juny.finalboard.domain.post.announcement.dto.PageInfo;
import com.juny.finalboard.domain.post.announcement.dto.ResAnnouncementPost;
import com.juny.finalboard.domain.post.announcement.dto.ResAnnouncementPostList;
import com.juny.finalboard.domain.post.announcement.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.entity.AnnouncementPost;
import java.time.LocalDateTime;
import java.util.List;

public class AnnouncementPostMapper {

  public static ResAnnouncementPost toResAnnouncementPost(AnnouncementPost post) {

    Boolean isNew = LocalDateTime.now().minusDays(7).isBefore(post.getCreatedAt());

    return ResAnnouncementPost.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .viewCount(post.getViewCount())
        .isPinned(post.getIsPinned())
        .createdBy(post.getCreatedBy())
        .createdAt(post.getCreatedAt().toString())
        .categoryName(post.getAnnouncementCategory().getName())
        .isNew(isNew)
        .build();
  }

  public static ResAnnouncementPostList toResAnnouncementPostList(
      List<AnnouncementPost> postList, SearchCondition searchCondition, long totalBoardCount) {

    PageInfo pageInfo =
        PageInfo.builder()
            .page(searchCondition.page())
            .pageSize(searchCondition.pageSize())
            .totalPages((int) Math.ceil((double) totalBoardCount / searchCondition.pageSize()))
            .totalCount(totalBoardCount)
            .build();

    List<ResAnnouncementPost> resPostList =
        postList.stream().map(AnnouncementPostMapper::toResAnnouncementPost).toList();

    return new ResAnnouncementPostList(resPostList, pageInfo);
  }
}
