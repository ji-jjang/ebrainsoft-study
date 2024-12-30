package com.juny.finalboard.domain.post.free.user.controller;

import com.juny.finalboard.domain.post.free.common.dto.FreeSearchCondition;
import com.juny.finalboard.domain.post.free.common.dto.FreeUpdateVO;
import com.juny.finalboard.domain.post.free.common.dto.ReqCreateFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ReqGetFreePostList;
import com.juny.finalboard.domain.post.free.common.dto.ReqUpdateFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ResFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ResFreePostList;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import com.juny.finalboard.domain.post.free.common.mapper.FreePostMapper;
import com.juny.finalboard.domain.post.free.common.service.FreePostService;
import com.juny.finalboard.domain.post.free.common.service.LocalFileService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class UserFreePostController {

  private final FreePostService freePostService;
  private final LocalFileService localFileService;

  /**
   *
   *
   * <h1>자유 게시글 생성 </h1>
   *
   * @param req 게시글 생성 폼
   * @param userDetails 유저 인증 정보
   * @return 생성된 자유 게시글
   */
  @PostMapping("/v1/free-posts")
  public ResponseEntity<ResFreePost> createFreePost(
      @ModelAttribute ReqCreateFreePost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    FreePost freePost = freePostService.createFreePost(req, userDetails.getId());

    ResFreePost resFreePost = FreePostMapper.toResFreePost(freePost);

    localFileService.saveFile(req.attachments(), resFreePost.attachmentList());

    return new ResponseEntity<>(resFreePost, HttpStatus.CREATED);
  }

  /**
   *
   *
   * <h1>자유 게시글 단건 조회 </h1>
   *
   * @param postId 게시글 아이디
   * @return 자유 게시글
   */
  @GetMapping("/v1/free-posts/{postId}")
  public ResponseEntity<ResFreePost> getFreePostWithCommentAndAttachment(
      @PathVariable Long postId) {

    freePostService.increaseViewCount(postId);

    FreePost freePost = freePostService.getFreePostDetail(postId);

    ResFreePost resFreePost = FreePostMapper.toResFreePost(freePost);

    return new ResponseEntity<>(resFreePost, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>자유 게시글 목록 조회 </h1>
   *
   * @param req 목록 조회 시 요청 DTO
   * @return 자유 게시글 목록
   */
  @GetMapping("/v1/free-posts")
  public ResponseEntity<ResFreePostList> getFreePostList(@ModelAttribute ReqGetFreePostList req) {

    FreeSearchCondition searchCondition = freePostService.createSearchCondition(req);

    long totalPostCount = freePostService.getTotalPostCount(searchCondition);

    List<FreePost> freePostList = freePostService.getFreePostListBySearchCondition(searchCondition);

    ResFreePostList resFreePostList =
        FreePostMapper.toResFreePostList(freePostList, searchCondition, totalPostCount);

    return new ResponseEntity<>(resFreePostList, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>자유 게시글 수정 </h1>
   *
   * @param postId 게시글 아이디
   * @param req 수정 폼
   * @param userDetails 사용자 인증 정보
   * @return 수정된 게시글
   */
  @PatchMapping("/v1/free-posts/{postId}")
  public ResponseEntity<ResFreePost> updateFreePost(
      @PathVariable Long postId,
      @ModelAttribute ReqUpdateFreePost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    FreePost freePost = freePostService.getFreePostDetail(postId);

    FreeUpdateVO freeUpdateVO = freePostService.updatePost(req, freePost, userDetails.getId());

    ResFreePost resFreePost = FreePostMapper.toResFreePost(freeUpdateVO.updateFreePost());

    localFileService.saveFile(
        freeUpdateVO.addMultipartFileList(), freeUpdateVO.addAttachmentList());
    localFileService.deleteFile(freeUpdateVO.deleteAttachmentList());

    return new ResponseEntity<>(resFreePost, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>자유 게시글 삭제 </h1>
   *
   * @param postId 게시글 아이디
   * @param userDetails 인증 정보
   * @return void
   */
  @DeleteMapping("/v1/free-posts/{postId}")
  public ResponseEntity<Void> deleteFreePost(
      @PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

    FreePost freePost = freePostService.getFreePostDetail(postId);

    freePostService.deletePostById(postId, freePost, userDetails.getId());

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
