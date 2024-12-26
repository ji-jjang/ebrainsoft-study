package com.juny.finalboard.domain.post.free.common.mapper;

import com.juny.finalboard.domain.post.free.common.dto.FreePageInfo;
import com.juny.finalboard.domain.post.free.common.dto.FreeSearchCondition;
import com.juny.finalboard.domain.post.free.common.dto.ResFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ResFreePostList;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.List;

public class FreePostMapper {

  /**
   *
   *
   * <h1>자유 게시글 단건 변환 (첨부파일, 댓글 수, 새로운 게시글 판단) </h1>
   *
   * @param freePost 자유 게시글
   * @return ResFreePost
   */
  public static ResFreePost toResFreePost(FreePost freePost) {

    Boolean isNew = LocalDateTime.now().minusDays(7).isBefore(freePost.getCreatedAt());

    Boolean hasAttachment =
        freePost.getFreeCommentList() != null && !freePost.getFreeCommentList().isEmpty();

    Integer commentCount =
        freePost.getFreeCommentList() == null ? 0 : freePost.getFreeCommentList().size();

    return ResFreePost.builder()
        .id(freePost.getId())
        .title(freePost.getTitle())
        .content(freePost.getContent())
        .viewCount(freePost.getViewCount())
        .createdBy(freePost.getCreatedBy())
        .createdAt(freePost.getCreatedAt().toString())
        .categoryId(freePost.getFreePostCategory().getId())
        .categoryName(freePost.getFreePostCategory().getName())
        .isNew(isNew)
        .hasAttachment(hasAttachment)
        .commentCount(commentCount)
        .attachmentList(freePost.getFreeAttachmentList())
        .build();
  }

  /**
   *
   *
   * <h1>자유 게시글 목록 변환 </h1>
   *
   * @param freePostList 자유 게시글 목록
   * @param searchCondition 검색 조건
   * @param totalPostCount 전체 게시글 수
   * @return ResFreePostList
   */
  public static ResFreePostList toResFreePostList(
      List<FreePost> freePostList, FreeSearchCondition searchCondition, long totalPostCount) {

    String[] sort = searchCondition.sort().split(Constants.SPACE_SIGN);

    FreePageInfo pageInfo =
        FreePageInfo.builder()
            .page(searchCondition.page() + 1)
            .pageSize(searchCondition.pageSize())
            .totalPages((int) Math.ceil((double) totalPostCount / searchCondition.pageSize()))
            .totalCount(totalPostCount)
            .build();

    FreeSearchCondition resSearchCondition =
        FreeSearchCondition.builder()
            .startDate(searchCondition.startDate().split(Constants.SPACE_SIGN)[0])
            .endDate(searchCondition.endDate().split(Constants.SPACE_SIGN)[0])
            .categoryId(searchCondition.categoryId())
            .keyword(searchCondition.keyword())
            .pageSize(searchCondition.pageSize())
            .sort(sort[0] + Constants.COLON_SIGN + sort[1])
            .build();

    List<ResFreePost> resFreePostList =
        freePostList.stream().map(FreePostMapper::toResFreePost).toList();

    return new ResFreePostList(resFreePostList, resSearchCondition, pageInfo);
  }
}
