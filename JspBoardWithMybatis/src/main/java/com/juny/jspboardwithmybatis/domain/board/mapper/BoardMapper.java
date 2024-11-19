package com.juny.jspboardwithmybatis.domain.board.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

  void increaseViewCount(Long boardId);

  List<Map<String, Object>> findBoardDetailById(Long id);
}
