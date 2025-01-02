package com.juny.finalboard.domain.post.question.common.service;

import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPostList;
import com.juny.finalboard.domain.post.question.common.dto.ReqUpdateQuestionPost;
import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.repository.AnswerRepository;
import com.juny.finalboard.domain.post.question.common.repository.QuestionCategoryRepository;
import com.juny.finalboard.domain.post.question.common.repository.QuestionPostRepository;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.domain.user.common.UserRepository;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private static final List<String> sortParams = List.of("created_at", "view_count");
  private static final List<String> sortDirections = List.of("asc", "desc");

  private final UserRepository userRepository;
  private final QuestionPostRepository questionPostRepository;
  private final QuestionCategoryRepository questionCategoryRepository;
  private final AnswerRepository answerRepository;

  /**
   *
   *
   * <h1>질문 생성 </h1>
   *
   * @param req 질문 생성 폼
   * @param userId 유저 아이디
   * @return 생성된 질문
   */
  @Transactional
  public QuestionPost createQuestionPost(ReqCreateQuestionPost req, Long userId) {

    User user = getUser(userId);

    QuestionCategory questionCategory = getQuestionCategory(req.categoryId());

    QuestionPost post =
        QuestionPost.builder()
            .title(req.title())
            .content(req.content())
            .viewCount(0)
            .isSecret(req.isSecret())
            .createdAt(LocalDateTime.now())
            .createdBy(user.getName())
            .questionCategory(questionCategory)
            .user(user)
            .build();

    questionPostRepository.save(post);

    return post;
  }

  /**
   *
   *
   * <h1>상세 조회 시 조회 수 증가</h1>
   *
   * @param postId 조회하는 게시글 아이디
   */
  public void increaseViewCount(Long postId) {

    questionPostRepository.increaseViewCount(postId);
  }

  /**
   *
   *
   * <h1>질문 상세 조회 </h1>
   *
   * @param postId 조회하는 아이디
   * @return 질문 상세
   */
  public QuestionPost getQuestionPostDetail(Long postId) {

    return questionPostRepository
        .findQuestionPostDetailById(postId)
        .orElseThrow(
            () -> new RuntimeException(String.format("post not found postId: %d", postId)));
  }

  /**
   *
   *
   * <h1>질문 목록 조회 시 검색 조건 생성 </h1>
   *
   * @param req 조회 시 검색 조건 DTO
   * @return 쿼리에 사용할 검색 조건 생성
   */
  public QuestionSearchCondition createSearchCondition(ReqGetQuestionPostList req) {

    String[] sort = req.sort().split(Constants.COLON_SIGN);

    if (sort.length != 2) {
      throw new RuntimeException("invalid sort params" + req.sort());
    }
    if (!sortParams.contains(sort[0]) || !sortDirections.contains(sort[1])) {
      throw new RuntimeException(String.format("not support sort %s %s", sort[0], sort[1]));
    }

    return QuestionSearchCondition.builder()
        .startDate(req.startDate() + " 00:00:00")
        .endDate(req.endDate() + " 23:59:59")
        .categoryId(req.categoryId())
        .keyword(req.keyword())
        .sort(sort[0] + Constants.SPACE_SIGN + sort[1])
        .page(req.page() - 1)
        .pageSize(req.pageSize())
        .build();
  }

  /**
   *
   *
   * <h1>검색 조건에 해당하는 게시글 전체 수 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 게시글 전체 수
   */
  public long getTotalPostCountBySearchCondition(QuestionSearchCondition searchCondition) {

    return questionPostRepository.getTotalQuestionPostCountBySearchCondition(searchCondition, -1);
  }

  /**
   *
   *
   * <h1>질문 목록 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 질문 목록
   */
  public List<QuestionPost> getQuestionPostListBySearchCondition(
      QuestionSearchCondition searchCondition) {

    int offset = searchCondition.page() * searchCondition.pageSize();

    return questionPostRepository.findAllWithBySearchCondition(searchCondition, offset);
  }

  /**
   *
   *
   * <h1>질문 수정 </h1>
   *
   * @param req 수정 폼
   * @param questionPost 기존 질문 조회
   * @param userId 유저 아이디
   * @return 수정된 질문
   */
  @Transactional
  public QuestionPost updatePost(
      ReqUpdateQuestionPost req, QuestionPost questionPost, Long userId) {

    User user = getUser(userId);

    QuestionCategory questionCategory = getQuestionCategory(req.categoryId());

    if (user.getRole().equals(Constants.USER_ROLE)) {
      if (!questionPost.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    QuestionPost updateQuestionPost =
        questionPost.toBuilder()
            .title(req.title())
            .content(req.content())
            .isSecret(req.isSecret())
            .questionCategory(questionCategory)
            .build();

    questionPostRepository.updatePost(updateQuestionPost);

    return updateQuestionPost;
  }

  /**
   *
   *
   * <h1>질문 삭제 </h1>
   *
   * @param postId 삭제할 아이디
   * @param post 삭제할 질문
   * @param userId 유저 아이디
   */
  @Transactional
  public void deletePost(Long postId, QuestionPost post, Long userId) {

    User user = getUser(userId);

    if (user.getRole().equals(Constants.USER_ROLE)) {
      if (!post.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    if (user.getRole().equals(Constants.USER_ROLE) && post.getAnswer() != null) {
      throw new RuntimeException("answered post is not allowed to delete");
    }

    if (post.getAnswer() != null) {
      answerRepository.deleteById(post.getAnswer().getId());
    }

    questionPostRepository.deletePostById(postId);
  }

  private User getUser(Long userId) {

    return userRepository
        .findById(userId)
        .orElseThrow(
            () -> new RuntimeException(String.format("user not found userId: %d", userId)));
  }

  private QuestionCategory getQuestionCategory(String req) {

    return questionCategoryRepository
        .findById(Long.parseLong(req))
        .orElseThrow(
            () -> new RuntimeException(String.format("category not found categoryId: %s", req)));
  }
}
