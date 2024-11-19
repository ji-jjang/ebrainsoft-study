package com.juny.jspboardwithmybatis.domain.board.service;

import com.juny.jspboardwithmybatis.domain.board.converter.BoardDTOConverter;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.mapper.BoardMapper;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  private final BoardMapper boardMapper;

  public BoardService(BoardMapper boardMapper) {
    this.boardMapper = boardMapper;
  }

  /**
   * <h1> 게시판 상세 페이지 조회 </h1>
   * @param id
   * @return ResBoardDetail
   */
  public ResBoardDetail getBoard(Long id) {

    List<Map<String, Object>> boards = boardMapper.findBoardDetailById(id);

    return BoardDTOConverter.convertToResBoardDetail(id, boards);
  }

  /**
   * <h1> 게시판 상세 페이지 조회 시 조회수 증가</h1>
   * @param id
   */
  public void increaseViewCount(Long id) {

    boardMapper.increaseViewCount(id);
  }
}
