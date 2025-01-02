package com.juny.finalboard.domain.post.question.common.mapper;

import com.juny.finalboard.domain.post.question.common.dto.QuestionPageInfo;
import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPostList;
import com.juny.finalboard.domain.post.question.common.entity.Answer;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.List;

public class QuestionPostMapper {

  public static ResQuestionPost toResQuestionPost(QuestionPost post) {

    boolean isNew = LocalDateTime.now().minusDays(7).isBefore(post.getCreatedAt());

    boolean isAnswered = (post.getAnswer() != null) ? true : false;

    Long postUserId = post.getUser() != null ? post.getUser().getId() : null;

    Answer answer = post.getAnswer() != null ? post.getAnswer() : null;

    return ResQuestionPost.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .isSecret(post.getIsSecret())
        .viewCount(post.getViewCount() + 1)
        .createdAt(post.getCreatedAt().toString())
        .createdBy(post.getCreatedBy())
        .isNew(isNew)
        .isAnswered(isAnswered)
        .category(post.getQuestionCategory())
        .userId(postUserId)
        .answer(answer)
        .build();
  }

  public static ResQuestionPostList toResQuestionPostList(
      List<QuestionPost> questionPosts,
      QuestionSearchCondition searchCondition,
      long totalPostCount) {

    String[] sort = searchCondition.sort().split(Constants.SPACE_SIGN);
    if (sort[0].startsWith("p."))
      sort[0] = sort[0].substring(2);

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
