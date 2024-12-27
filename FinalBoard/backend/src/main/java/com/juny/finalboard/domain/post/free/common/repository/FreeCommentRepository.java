package com.juny.finalboard.domain.post.free.common.repository;

import com.juny.finalboard.domain.post.free.common.entity.FreeComment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FreeCommentRepository {

  void save(FreeComment comment);

  void deleteCommentById(Long id);

  List<FreeComment> findCommentsByPostIds(List<Long> postIds);
}
