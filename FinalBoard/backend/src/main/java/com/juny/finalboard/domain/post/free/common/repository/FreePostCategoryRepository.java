package com.juny.finalboard.domain.post.free.common.repository;

import com.juny.finalboard.domain.post.free.common.entity.FreePostCategory;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FreePostCategoryRepository {

  Optional<FreePostCategory> findById(Long id);

  List<FreePostCategory> findAll();
}
