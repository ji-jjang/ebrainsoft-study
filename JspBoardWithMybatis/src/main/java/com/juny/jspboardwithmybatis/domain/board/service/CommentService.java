package com.juny.jspboardwithmybatis.domain.board.service;

import com.juny.jspboardwithmybatis.domain.board.dto.ReqCommentCreate;
import com.juny.jspboardwithmybatis.domain.board.entity.Comment;
import com.juny.jspboardwithmybatis.domain.board.mapper.CommentMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentMapper commentMapper;

  /**
   *
   *
   * <h1>댓글 생성 DB 반영 </h1>
   *
   * @param boardId
   * @param req
   */
  public void createComment(Long boardId, ReqCommentCreate req) {

    Comment comment =
        new Comment(
            req.getContent(), req.getPassword(), LocalDateTime.now(), req.getCreatedBy(), boardId);

    commentMapper.saveComment(comment);
  }
}
