package com.juny.finalboard.domain.post.announcement.common.mapper;

import com.juny.finalboard.domain.post.announcement.common.dto.PageInfo;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementPostList;
import com.juny.finalboard.domain.post.announcement.common.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.List;

public class AnnouncementPostMapper {

  /**
   *
   *
   * <h1>공지사항 게시글 View 정보로 반환 매퍼</h1>
   *
   * @param post 게시글
   * @return 게시글 반환 정보
   */
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
        .categoryId(post.getAnnouncementCategory().getId())
        .categoryName(post.getAnnouncementCategory().getName())
        .isNew(isNew)
        .build();
  }

  /**
   *
   *
   * <h1>공지사항 게시판 목록 View 정보로 반환 매퍼</h1>
   *
   * @param pinnedPostList 고정된 게시글 리스트
   * @param unPinnedPostList 고정되지 않은 게시글 리스트
   * @param searchCondition 검색 조건
   * @param totalBoardCount 전체 반환
   * @return 게시판 목록 반환 정보 {게시글 목록, 검색 조건, 페이지 정보}
   */
  public static ResAnnouncementPostList toResAnnouncementPostList(
      List<AnnouncementPost> pinnedPostList,
      List<AnnouncementPost> unPinnedPostList,
      SearchCondition searchCondition,
      long totalBoardCount) {

    String[] sort = searchCondition.sort().split(Constants.SPACE_SIGN);

    PageInfo pageInfo =
        PageInfo.builder()
            .page(searchCondition.page() + 1)
            .pageSize(searchCondition.pageSize())
            .totalPages((int) Math.ceil((double) totalBoardCount / searchCondition.pageSize()))
            .totalCount(totalBoardCount)
            .build();

    SearchCondition resSearchCondition =
        SearchCondition.builder()
            .startDate(searchCondition.startDate().split(Constants.SPACE_SIGN)[0])
            .endDate(searchCondition.endDate().split(Constants.SPACE_SIGN)[0])
            .categoryId(searchCondition.categoryId())
            .keyword(searchCondition.keyword())
            .pageSize(searchCondition.pageSize())
            .sort(sort[0] + Constants.COLON_SIGN + sort[1])
            .build();

    List<ResAnnouncementPost> resPinnedPostList =
        pinnedPostList.stream().map(AnnouncementPostMapper::toResAnnouncementPost).toList();

    List<ResAnnouncementPost> resUnpinnedPostList =
        unPinnedPostList.stream().map(AnnouncementPostMapper::toResAnnouncementPost).toList();

    return new ResAnnouncementPostList(
        resPinnedPostList, resUnpinnedPostList, resSearchCondition, pageInfo);
  }
}
