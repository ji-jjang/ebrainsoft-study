package com.juny.board.domain.board.mapper;

import com.juny.board.domain.board.dto.ResComment;
import com.juny.board.domain.board.entity.Comment;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  ResComment toResComment(Comment comment);

  List<ResComment> toResCommentList(List<Comment> commentList);
}
