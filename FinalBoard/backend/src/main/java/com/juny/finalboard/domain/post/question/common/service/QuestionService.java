package com.juny.finalboard.domain.post.question.common.service;

import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqDeleteQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPostList;
import com.juny.finalboard.domain.post.question.common.dto.ReqUpdateQuestionPost;
import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.repository.QuestionCategoryRepository;
import com.juny.finalboard.domain.post.question.common.repository.QuestionPostRepository;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.domain.user.common.UserRepository;
import com.juny.finalboard.global.constant.Constants;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   *
   *
   * <h1>질문 생성 </h1>
   *
   * <br>
   * - 로그인 하지 않고, 비밀글로 한 사용자는 비밀번호 요청 폼 추가 입력 <br>
   *
   * @param req 질문 생성 폼
   * @param userDetails 유저 인증 정보
   * @return 생성된 질문
   */
  @Transactional
  public QuestionPost createQuestionPost(ReqCreateQuestionPost req, CustomUserDetails userDetails) {

    String userName = Constants.ANONYMOUS_NAME;
    Long userId = null;

    if (userDetails != null) {
      userName = userDetails.getName();
      userId = userDetails.getId();
    }

    QuestionCategory questionCategory = getQuestionCategory(req.categoryId());

    QuestionPost post =
        QuestionPost.builder()
            .title(req.title())
            .content(req.content())
            .viewCount(0)
            .isSecret(req.isSecret())
            .createdAt(LocalDateTime.now())
            .createdBy(userName)
            .questionCategory(questionCategory)
            .user(User.builder().id(userId).build())
            .build();

    if (userId == null) {
      if (req.password().isEmpty() || req.passwordConfirm().isEmpty())
        throw new RuntimeException("retry with password");

      if (!req.password().equals(req.passwordConfirm()))
        throw new RuntimeException("password not match");

      post = post.toBuilder().password(bCryptPasswordEncoder.encode(req.password())).build();
    }

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
   * <h1>질문 게시글 상세 조회 </h1>
   *
   * <br>
   * - 관리자인 경우 권한 조회 없이 바로 게시글 반환 <br>
   * - 게시글에 비밀번호 있고(익명 사용자) 비밀글인 경우, 1. 비밀번호 요청값 empty -> password 로 다시 요청 하도록, 2. 패스워드 일치여부 검사 <br>
   * - 게시글에 비밀번호 없고 비밀글인 경우, 게시글의 유저 아이디와 현재 접근한 사용자 아이디와 다른 경우 -> "user not match"
   *
   * @param postId 조회할 게시글 아이디
   * @param req 비밀번호
   * @param userDetails 인증 정보
   * @return 상세 게시글
   */
  public QuestionPost getQuestionPostDetail(
      Long postId, ReqGetQuestionPost req, CustomUserDetails userDetails) {

    QuestionPost post =
        questionPostRepository
            .findQuestionPostDetailById(postId)
            .orElseThrow(
                () -> new RuntimeException(String.format("post not found postId: %d", postId)));

    Long userId = null;
    String userRole = null;
    if (userDetails != null) {
      userId = userDetails.getId();
      userRole = userDetails.getRole();
    }

    if (userId != null && userRole.equals(Constants.ADMIN_ROLE)) return post;

    if (post.getPassword() != null && post.getIsSecret()) {
      if (req.password().isEmpty()) {
        throw new RuntimeException("retry with password");
      }

      if (!validatePassword(req.password(), post.getPassword())) {
        throw new RuntimeException("password not match");
      }
    }

    if (post.getPassword() == null && post.getIsSecret()) {
      if (!post.getUser().getId().equals(userId)) {
        throw new RuntimeException(
            String.format("user not match, post's userId: %d", post.getUser().getId()));
      }
    }

    return post;
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

    if (sort[0].equals("created_at")) {
      sort[0] = "p." + sort[0];
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
   * <br>
   * - 게시글에 비밀번호가 있다면, 작성자가 익명이므로 비밀번호 검사<br>
   * - 비밀번호 없다면, 작성자 아이디와 로그인한 사용자 아이디 검사<br>
   * - 관리자라면 검증 로직 생략 (패스워드 확인 및 사용자 아이디)
   *
   * @param req 수정 폼
   * @param post 기존 질문 조회
   * @param userDetails 인증 정보
   * @return 수정된 질문
   */
  @Transactional
  public QuestionPost updatePost(
      ReqUpdateQuestionPost req, QuestionPost post, CustomUserDetails userDetails) {

    QuestionCategory questionCategory = getQuestionCategory(req.categoryId());

    Long userId = null;
    String userRole = null;
    if (userDetails != null) {
      userId = userDetails.getId();
      userRole = userDetails.getRole();
    }

    if (post.getPassword() != null) {

      if (req.password().isEmpty()) {
        throw new RuntimeException("retry with password");
      }

      boolean isValid = bCryptPasswordEncoder.matches(req.password(), post.getPassword());
      if (!isValid) {
        throw new RuntimeException("password not match");
      }
    }

    if (post.getPassword() == null && userRole.equals(Constants.USER_ROLE)) {
      if (!post.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    QuestionPost updateQuestionPost =
        post.toBuilder()
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
   * - 게시글에 비밀번호가 있다면, 작성자가 익명이므로 비밀번호 검사<br>
   * - 비밀번호 없다면, 작성자 아이디와 로그인한 사용자 아이디 검사<br>
   * - 관리자라면 검증 로직 생략 (패스워드 확인 및 사용자 아이디)
   *
   * @param req 삭제 요청 폼(패스워드)
   * @param post 삭제할 질문
   * @param userDetails 유저 인증 정보
   */
  @Transactional
  public void deletePost(
      ReqDeleteQuestionPost req, QuestionPost post, CustomUserDetails userDetails) {

    Long userId = null;
    String userRole = null;
    if (userDetails != null) {
      userId = userDetails.getId();
      userRole = userDetails.getRole();
    }

    if (post.getPassword() != null) {

      if (req.password().isEmpty()) {
        throw new RuntimeException("retry with password");
      }

      boolean isValid = validatePassword(req.password(), post.getPassword());

      if (!isValid) {
        throw new RuntimeException("password not match");
      }
    }

    if (post.getPassword() == null && userRole.equals(Constants.USER_ROLE)) {
      if (!post.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    questionPostRepository.deletePostById(post.getId());
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

  private boolean validatePassword(String inputPassword, String password) {

    return bCryptPasswordEncoder.matches(inputPassword, password);
  }
}
