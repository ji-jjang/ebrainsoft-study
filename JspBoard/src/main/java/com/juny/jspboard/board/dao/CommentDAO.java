package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.entity.Comment;
import java.sql.Connection;
import java.util.List;

public interface CommentDAO {
  List<Comment> getComments(Connection conn, Long boardId);

  List<Comment> getComments(Long boardId);

  void createComment(Comment comment);
}
