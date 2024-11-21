package com.juny.jspboardwithmybatis.domain.board.mapper;

import com.juny.jspboardwithmybatis.domain.board.entity.Board;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

  void increaseViewCount(Long boardId);

  Map<String, Object> findBoardDetailById(Long id);

  long getTotalBoardCount(Map<String, Object> searchConditions);

  List<Map<String, Object>> getBoardList(Map<String, Object> searchConditions);

  void saveBoard(Board board);

  void updateBoard(Board board);

  void deleteBoardById(Long id);
}
