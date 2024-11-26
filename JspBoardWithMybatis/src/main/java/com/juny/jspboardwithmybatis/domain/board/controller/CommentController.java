package com.juny.jspboardwithmybatis.domain.board.controller;

import com.juny.jspboardwithmybatis.domain.board.dto.ReqCommentCreate;
import com.juny.jspboardwithmybatis.domain.board.service.CommentService;
import com.juny.jspboardwithmybatis.global.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
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
   * @param reqCommentCreate
   * @return View
   */
  @PostMapping("/boards/{boardId}/comments")
  public String createComment(
      @PathVariable Long boardId, @ModelAttribute ReqCommentCreate reqCommentCreate) {

    commentService.createComment(boardId, reqCommentCreate);

    return Constants.REDIRECT_PREFIX + "/boards/{boardId}";
  }
}
