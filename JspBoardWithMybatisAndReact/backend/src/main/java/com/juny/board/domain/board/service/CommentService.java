package com.juny.board.domain.board.service;

import com.juny.board.domain.board.entity.Comment;
import com.juny.board.domain.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  /**
   *
   *
   * <h1>댓글 생성 DB 반영 </h1>
   *
   * @param comment
   */
  public void createComment(Comment comment) {

    commentRepository.saveComment(comment);
  }
}
