package com.juny.board.domain.board.controller;

import com.juny.board.domain.board.dto.ReqCommentCreate;
import com.juny.board.domain.board.entity.Comment;
import com.juny.board.domain.board.service.CommentService;
import java.time.LocalDateTime;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  /**
   *
   *
   * <h1>댓글 생성 컨트롤러 </h1>
   *
   * @param boardId
   * @param req
   * @return void
   */
  @PostMapping("/boards/{boardId}/comments")
  public void createComment(
      @PathVariable Long boardId, @Validated @ModelAttribute ReqCommentCreate req) {

    Comment comment =
        Comment.builder()
            .content(req.getContent())
            .password(req.getPassword())
            .createdAt(LocalDateTime.now())
            .createdBy(req.getPassword())
            .boardId(boardId)
            .build();

    commentService.createComment(comment);
  }
}
