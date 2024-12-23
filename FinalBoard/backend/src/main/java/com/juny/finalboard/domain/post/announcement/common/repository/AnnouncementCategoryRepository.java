package com.juny.finalboard.domain.post.announcement.common.repository;

import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementCategory;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementCategoryRepository {

  Optional<AnnouncementCategory> findById(Long id);

  List<AnnouncementCategory> findAll();
}
