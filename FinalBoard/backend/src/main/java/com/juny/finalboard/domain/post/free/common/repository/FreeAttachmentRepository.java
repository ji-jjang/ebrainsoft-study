package com.juny.finalboard.domain.post.free.common.repository;

import com.juny.finalboard.domain.post.free.common.entity.FreeAttachment;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FreeAttachmentRepository {

  void save(FreeAttachment freeAttachment);

  Optional<FreeAttachment> findById(Long id);

  void deleteById(Long id);

  List<FreeAttachment> findAttachmentsByPostIds(List<Long> postIds);
}
