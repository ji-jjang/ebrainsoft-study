package com.juny.finalboard.domain.post.free.common.service;

import com.juny.finalboard.domain.post.free.common.entity.FreePostCategory;
import com.juny.finalboard.domain.post.free.common.repository.FreePostCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreePostCategoryService {

  private final FreePostCategoryRepository freePostCategoryRepository;

  /**
   *
   *
   * <h1>모든 카테고리 id, 이름 반환 </h1>
   *
   * @return List<FreePostCategory>
   */
  public List<FreePostCategory> getAllCategories() {

    return freePostCategoryRepository.findAll();
  }
}
