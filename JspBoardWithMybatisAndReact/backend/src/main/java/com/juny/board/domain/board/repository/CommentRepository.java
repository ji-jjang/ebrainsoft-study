package com.juny.board.domain.board.repository;

import com.juny.board.domain.board.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentRepository {

  void saveComment(Comment comment);

  void deleteCommentById(Long id);
}
