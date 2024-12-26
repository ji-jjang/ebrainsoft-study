package com.juny.finalboard.domain.post.announcement.common.repository;

import com.juny.finalboard.domain.post.announcement.common.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnnouncementPostRepository {

  long getTotalAnnouncementPostCount(
      @Param("searchCondition") SearchCondition searchCondition, @Param("offset") int offset);

  List<AnnouncementPost> findAllWithCategoryBySearchCondition(
      @Param("searchCondition") SearchCondition searchCondition, @Param("offset") int offset);

  void save(AnnouncementPost announcementPost);

  void deleteAnnouncementPostById(Long id);

  Optional<AnnouncementPost> findPostDetailById(Long id);

  void updatePost(AnnouncementPost post);

  List<AnnouncementPost> findPinnedPostList(Integer count);

  void increaseViewCount(Long id);
}
