package com.juny.finalboard.domain.post.announcement.admin.service;

import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostCreate;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostUpdate;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.repository.AnnouncementCategoryRepository;
import com.juny.finalboard.domain.post.announcement.common.repository.AnnouncementPostRepository;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.domain.user.common.UserRepository;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAnnouncementPostService {

  private final AnnouncementPostRepository announcementPostRepository;
  private final AnnouncementCategoryRepository announcementCategoryRepository;
  private final UserRepository userRepository;

  /**
   *
   *
   * <h1>게시글 생성 </h1>
   *
   * @param req 생성 DTO
   * @param userId 유저 아이디
   * @return 생성된 게시글
   */
  public AnnouncementPost createPost(ReqPostCreate req, Long userId) {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException(String.format("invalid user id: %d", userId)));

    AnnouncementCategory category =
        announcementCategoryRepository
            .findById(req.categoryId())
            .orElseThrow(
                () ->
                    new RuntimeException(
                        String.format("invalid category id: %d", req.categoryId())));

    AnnouncementPost announcementPost =
        AnnouncementPost.builder()
            .title(req.title())
            .content(req.content())
            .viewCount(0)
            .isPinned(req.isPinned())
            .createdBy(user.getName())
            .createdAt(LocalDateTime.now())
            .announcementCategory(category)
            .user(User.builder().id(userId).build())
            .build();

    announcementPostRepository.save(announcementPost);

    return announcementPost;
  }

  /**
   *
   *
   * <h1>게시글 수정 </h1>
   *
   * @param req 수정 DTO
   * @param postId 게시글 ID
   * @param userId 사용자 ID
   * @return 수정된 게시글
   */
  public AnnouncementPost updatePost(ReqPostUpdate req, Long postId, Long userId) {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException(String.format("invalid user id: %d", userId)));

    AnnouncementPost post =
        announcementPostRepository
            .findPostDetailById(postId)
            .orElseThrow(() -> new RuntimeException(String.format("invalid post id: %d", postId)));

    if (user.getRole().equals(Constants.USER_ROLE)) {
      if (!userId.equals(post.getUser().getId())) {
        throw new RuntimeException(
            String.format("post userId: %d userId: %d not match", post.getUser().getId(), userId));
      }
    }

    AnnouncementCategory category =
        announcementCategoryRepository
            .findById(req.categoryId())
            .orElseThrow(() -> new RuntimeException("category not found"));

    AnnouncementPost updatedPost =
        post.toBuilder()
            .title(req.title())
            .content(req.content())
            .isPinned(req.isPinned())
            .announcementCategory(category)
            .build();

    announcementPostRepository.updatePost(updatedPost);

    return updatedPost;
  }

  /**
   *
   *
   * <h1>게시글 삭제 </h1>
   *
   * @param id 게시글 ID
   */
  public void deleteAnnouncementPostById(Long id) {

    announcementPostRepository.deleteAnnouncementPostById(id);
  }
}
