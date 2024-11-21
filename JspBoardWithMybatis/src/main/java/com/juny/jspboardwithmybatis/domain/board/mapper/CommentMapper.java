package com.juny.jspboardwithmybatis.domain.board.mapper;

import com.juny.jspboardwithmybatis.domain.board.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

  void saveComment(Comment comment);

  void deleteCommentById(Long id);
}
