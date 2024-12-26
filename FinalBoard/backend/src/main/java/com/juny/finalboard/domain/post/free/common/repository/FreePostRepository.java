package com.juny.finalboard.domain.post.free.common.repository;

import com.juny.finalboard.domain.post.free.common.dto.FreeSearchCondition;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FreePostRepository {

  Optional<FreePost> findFreePostDetailById(Long id);

  void save(FreePost freePost);

  long getTotalFreePostCount(
      @Param("searchCondition") FreeSearchCondition searchCondition, @Param("offset") int offset);

  List<FreePost> findAllWithBySearchCondition(
      @Param("searchCondition") FreeSearchCondition searchCondition, @Param("offset") int offset);

  void updatePost(FreePost updateFreePost);

  void deleteById(Long id);
}
