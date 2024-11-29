package com.juny.board.domain.board.repository;

import com.juny.board.domain.board.entity.Board;
import com.juny.board.domain.board.entity.vo.SearchConditionVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardRepository {

  void increaseViewCount(Long id);

  Board findBoardDetailById(Long id);

  long getTotalBoardCount(SearchConditionVO searchConditionVO);

  List<Board> getBoardList(SearchConditionVO searchConditionVO);

  void saveBoard(Board board);

  void updateBoard(Board board);

  void deleteBoardById(Long id);

  String getBoardPassword(Long id);
}
