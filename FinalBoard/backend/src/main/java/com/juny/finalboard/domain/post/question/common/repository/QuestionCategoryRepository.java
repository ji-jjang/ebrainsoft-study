package com.juny.finalboard.domain.post.question.common.repository;

import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionCategoryRepository {

  Optional<QuestionCategory> findById(Long id);

  List<QuestionCategory> findAll();
}
