package com.juny.finalboard.domain.post.question.common.dto;

import java.util.List;

public record ResQuestionPostList(
    List<ResQuestionPost> postList,
    QuestionSearchCondition searchCondition,
    QuestionPageInfo pageInfo) {}
