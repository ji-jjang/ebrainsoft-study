package com.juny.finalboard.domain.post.question.common.repository;

import com.juny.finalboard.domain.post.question.common.entity.Answer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnswerRepository {

  void save(Answer answer);

  void deleteById(Long id);
}
