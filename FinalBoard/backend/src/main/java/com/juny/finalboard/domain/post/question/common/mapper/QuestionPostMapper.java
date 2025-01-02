package com.juny.finalboard.domain.post.question.common.mapper;

import com.juny.finalboard.domain.post.question.common.dto.QuestionPageInfo;
import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPostList;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.List;

public class QuestionPostMapper {

  public static ResQuestionPost toResQuestionPost(QuestionPost post) {

    boolean isNew = LocalDateTime.now().minusDays(7).isBefore(post.getCreatedAt());

    return ResQuestionPost.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .isSecret(post.getIsSecret())
        .viewCount(post.getViewCount())
        .createdAt(post.getCreatedAt().toString())
        .createdBy(post.getCreatedBy())
        .isNew(isNew)
        .category(post.getQuestionCategory())
        .userId(post.getUser().getId())
        .build();
  }

  public static ResQuestionPostList toResQuestionPostList(
      List<QuestionPost> questionPosts,
      QuestionSearchCondition searchCondition,
      long totalPostCount) {

    String[] sort = searchCondition.sort().split(Constants.SPACE_SIGN);

    QuestionPageInfo resPageInfo =
        QuestionPageInfo.builder()
            .page(searchCondition.page() + 1)
            .pageSize(searchCondition.pageSize())
            .totalPages((int) Math.ceil((double) totalPostCount / searchCondition.pageSize()))
            .totalCount(totalPostCount)
            .build();

    QuestionSearchCondition resSearchCondition =
        QuestionSearchCondition.builder()
            .startDate(searchCondition.startDate().split(Constants.SPACE_SIGN)[0])
            .endDate(searchCondition.endDate().split(Constants.SPACE_SIGN)[0])
            .categoryId(searchCondition.categoryId())
            .keyword(searchCondition.keyword())
            .pageSize(searchCondition.pageSize())
            .sort(sort[0] + Constants.COLON_SIGN + sort[1])
            .build();

    List<ResQuestionPost> resQuestionPostList =
        questionPosts.stream().map(QuestionPostMapper::toResQuestionPost).toList();

    return new ResQuestionPostList(resQuestionPostList, resSearchCondition, resPageInfo);
  }
}
