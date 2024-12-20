package com.juny.finalboard.domain.post.announcement.repository;

import com.juny.finalboard.domain.post.announcement.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.entity.AnnouncementPost;
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
