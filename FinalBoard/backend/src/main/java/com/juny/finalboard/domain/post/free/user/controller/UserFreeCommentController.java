package com.juny.finalboard.domain.post.free.user.controller;

import com.juny.finalboard.domain.post.free.common.dto.ReqCreateFreeComment;
import com.juny.finalboard.domain.post.free.common.entity.FreeComment;
import com.juny.finalboard.domain.post.free.common.service.FreePostService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserFreeCommentController {

  private final FreePostService freePostService;

  @PostMapping("/api/v1/free-posts/{postId}/comments")
  public ResponseEntity<FreeComment> addComment(
      @PathVariable Long postId,
      @RequestBody ReqCreateFreeComment req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    FreeComment comment = freePostService.createComment(req, postId, userDetails.getId());

    return new ResponseEntity<>(comment, HttpStatus.OK);
  }
}
