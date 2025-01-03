package com.juny.finalboard.domain.post.question.user.controller;

import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqDeleteQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPostList;
import com.juny.finalboard.domain.post.question.common.dto.ReqUpdateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPostList;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.mapper.QuestionPostMapper;
import com.juny.finalboard.domain.post.question.common.service.QuestionService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserQuestionPostController {

  private final QuestionService questionService;

  /**
   *
   *
   * <h1>질문 게시글 생성 컨트롤러 </h1>
   *
   * @param req 질문 생성 폼
   * @param userDetails 유저 인증 정보
   * @return 생성된 질문
   */
  @PostMapping("/v1/question-posts")
  public ResponseEntity<ResQuestionPost> createQuestionPost(
      @ModelAttribute ReqCreateQuestionPost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post = questionService.createQuestionPost(req, userDetails);

    ResQuestionPost resQuestionPost = QuestionPostMapper.toResQuestionPost(post);

    return new ResponseEntity<>(resQuestionPost, HttpStatus.CREATED);
  }

  /**
   *
   *
   * <h1>질문 게시글 조회 컨트롤러 </h1>
   *
   * @param postId 조회할 게시글 아이디
   * @return 질문 상세
   */
  @PostMapping("/v1/question-posts/{postId}")
  public ResponseEntity<ResQuestionPost> getQuestionPost(
      @ModelAttribute ReqGetQuestionPost req,
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post = questionService.getQuestionPostDetail(postId, req, userDetails);

    questionService.increaseViewCount(postId);

    ResQuestionPost resQuestionPost = QuestionPostMapper.toResQuestionPost(post);

    return new ResponseEntity<>(resQuestionPost, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>질문 목록 조회 컨트롤러</h1>
   *
   * @param req 목록 조회 DTO (검색 파라미터)
   * @return 질문 목록
   */
  @GetMapping("/v1/question-posts")
  public ResponseEntity<ResQuestionPostList> getQuestionPostList(
      @ModelAttribute ReqGetQuestionPostList req) {

    QuestionSearchCondition searchCondition = questionService.createSearchCondition(req);

    long totalPostCount = questionService.getTotalPostCountBySearchCondition(searchCondition);

    List<QuestionPost> questionList =
        questionService.getQuestionPostListBySearchCondition(searchCondition);

    ResQuestionPostList resQuestionPostList =
        QuestionPostMapper.toResQuestionPostList(questionList, searchCondition, totalPostCount);

    return new ResponseEntity<>(resQuestionPostList, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>질문 수정 컨트롤러 </h1>
   *
   * @param postId 수정할 게시글 아이디
   * @param req 수정 폼
   * @param userDetails 유저 인증 정보
   * @return 수정된 질문
   */
  @PatchMapping("/v1/question-posts/{postId}")
  public ResponseEntity<ResQuestionPost> updateQuestionPost(
      @PathVariable Long postId,
      @ModelAttribute ReqUpdateQuestionPost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().password(req.password()).build(), userDetails);

    QuestionPost updatedPost = questionService.updatePost(req, post, userDetails);

    ResQuestionPost resQuestionPost = QuestionPostMapper.toResQuestionPost(updatedPost);

    return new ResponseEntity<>(resQuestionPost, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>질문 삭제 컨트롤러 </h1>
   *
   * @param postId 게시글 아이디
   * @param userDetails 인증 정보
   * @return Void
   */
  @PostMapping("/v1/question-posts/{postId}/delete")
  public ResponseEntity<Void> deleteQuestionPost(
      @PathVariable Long postId,
      @ModelAttribute ReqDeleteQuestionPost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().password(req.password()).build(), userDetails);

    questionService.deletePost(req, post, userDetails);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
