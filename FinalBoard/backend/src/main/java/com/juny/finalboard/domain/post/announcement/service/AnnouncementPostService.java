package com.juny.finalboard.domain.post.announcement.service;

import com.juny.finalboard.domain.post.announcement.dto.ReqGetPostList;
import com.juny.finalboard.domain.post.announcement.dto.ReqPostCreate;
import com.juny.finalboard.domain.post.announcement.dto.ReqPostUpdate;
import com.juny.finalboard.domain.post.announcement.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.entity.AnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.entity.AnnouncementPost;
import com.juny.finalboard.domain.post.announcement.repository.AnnouncementCategoryRepository;
import com.juny.finalboard.domain.post.announcement.repository.AnnouncementPostRepository;
import com.juny.finalboard.domain.user.User;
import com.juny.finalboard.domain.user.UserRepository;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementPostService {

  private static final List<String> sortParams = List.of("created_at", "view_count");
  private static final List<String> sortDirections = List.of("asc", "desc");
  private final AnnouncementPostRepository announcementPostRepository;
  private final AnnouncementCategoryRepository announcementCategoryRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;

  /**
   *
   *
   * <h1>게시글 목록 조회시 검색 조건 생성 </h1>
   *
   * @param req 조회 DTO
   * @return 검색 조건
   */
  public SearchCondition createSearchCondition(ReqGetPostList req) {

    String[] sort = req.sort().split(":");

    if (sort.length != 2) {
      throw new RuntimeException("invalid sort params" + req.sort());
    }
    if (!sortParams.contains(sort[0]) || !sortDirections.contains(sort[1])) {
      throw new RuntimeException(String.format("not support sort %s %s", sort[0], sort[1]));
    }

    return SearchCondition.builder()
        .startDate(req.startDate() + " 00:00:00")
        .endDate(req.endDate() + " 23:59:59")
        .categoryName(req.categoryName())
        .keyword(req.keyword())
        .sort(sort[0] + Constants.SPACE_SIGN + sort[1])
        .page(req.page() - 1)
        .pageSize(req.pageSize())
        .offset((req.page() - 1) * req.pageSize())
        .build();
  }

  /**
   *
   *
   * <h1>게시글 목록 조회 시 검색 조건에 따른 총 게시글 수 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 총 게시글 수
   */
  public long getTotalBoardCount(SearchCondition searchCondition) {

    return announcementPostRepository.getTotalAnnouncementPostCount(searchCondition);
  }

  /**
   *
   *
   * <h1>게시글 목록 조회 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 게시글 목록
   */
  public List<AnnouncementPost> getPostListBySearchCondition(SearchCondition searchCondition) {

    return announcementPostRepository.findAllWithCategoryBySearchCondition(searchCondition);
  }

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
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

    if (!req.password().equals(req.passwordConfirm())) {
      throw new RuntimeException("password not match");
    }

    AnnouncementCategory category =
        announcementCategoryRepository
            .findById(req.categoryId())
            .orElseThrow(() -> new RuntimeException("invalid categoryName id"));

    AnnouncementPost announcementPost =
        AnnouncementPost.builder()
            .title(req.title())
            .content(req.content())
            .password(bCryptPasswordEncoder.encode(req.password()))
            .viewCount(0)
            .isPinned(req.isPinned())
            .createdBy(user.getName())
            .createdAt(LocalDateTime.now())
            .announcementCategory(category)
            .build();

    announcementPostRepository.save(announcementPost);

    return announcementPost;
  }

  /**
   *
   *
   * <h1>게시글 단건 조회 </h1>
   *
   * @param id 게시글 ID
   * @return 게시글
   */
  public AnnouncementPost getPostById(Long id) {

    AnnouncementPost announcementPost =
        announcementPostRepository
            .findPostDetailById(id)
            .orElseThrow(() -> new RuntimeException(String.format("invalid post id: %d", id)));

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

    AnnouncementCategory category =
        announcementCategoryRepository
            .findById(req.categoryId())
            .orElseThrow(() -> new RuntimeException("category not found"));

    AnnouncementPost post =
        announcementPostRepository
            .findPostDetailById(postId)
            .orElseThrow(() -> new RuntimeException(String.format("invalid post id: %d", postId)));

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException(String.format("invalid user id: %d", userId)));

    AnnouncementPost updatedPost =
        post.toBuilder()
            .title(req.title())
            .content(req.content())
            .isPinned(req.isPinned())
            .announcementCategory(category)
            .createdBy(user.getName())
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
