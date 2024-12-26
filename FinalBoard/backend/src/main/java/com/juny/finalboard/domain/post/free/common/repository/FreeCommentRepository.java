package com.juny.finalboard.domain.post.free.common.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FreeCommentRepository {

  void deleteCommentById(Long id);
}
