package com.juny.finalboard.domain.post.question.common.repository;

import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionPostRepository {

  void save(QuestionPost post);

  void deletePostById(Long id);

  Long getTotalQuestionPostCountBySearchCondition(
      @Param("searchCondition") QuestionSearchCondition searchCondition,
      @Param("offset") int offset);

  List<QuestionPost> findAllWithBySearchCondition(
      @Param("searchCondition") QuestionSearchCondition searchCondition,
      @Param("offset") int offset);

  Optional<QuestionPost> findQuestionPostDetailById(Long id);

  void increaseViewCount(Long id);

  void updatePost(QuestionPost post);
}
