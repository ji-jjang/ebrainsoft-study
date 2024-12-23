package com.juny.finalboard.domain.post.announcement.common.repository;

import com.juny.finalboard.domain.post.announcement.common.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementPostRepository {

  long getTotalAnnouncementPostCount(SearchCondition searchCondition);

  List<AnnouncementPost> findAllWithCategoryBySearchCondition(SearchCondition searchCondition);

  void save(AnnouncementPost announcementPost);

  void deleteAnnouncementPostById(Long id);

  Optional<AnnouncementPost> findPostDetailById(Long id);

  void updatePost(AnnouncementPost post);
}
