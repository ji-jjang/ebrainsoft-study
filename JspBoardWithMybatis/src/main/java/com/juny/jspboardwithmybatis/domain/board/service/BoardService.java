package com.juny.jspboardwithmybatis.domain.board.service;

import com.juny.jspboardwithmybatis.domain.board.converter.BoardDTOConverter;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardList;
import com.juny.jspboardwithmybatis.domain.board.mapper.BoardMapper;
import com.juny.jspboardwithmybatis.domain.utils.DateFormatUtils;
import java.util.LinkedHashMap;
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
   *
   *
   * <h1>게시판 상세 페이지 조회 </h1>
   *
   * @param id
   * @return ResBoardDetail
   */
  public ResBoardDetail getBoard(Long id) {

    List<Map<String, Object>> board = boardMapper.findBoardDetailById(id);

    for (var e : board) {
      System.out.println("e.entrySet() = " + e.entrySet());
    }
    return BoardDTOConverter.convertToResBoardDetail(id, board);
  }

  /**
   *
   *
   * <h1>게시판 상세 페이지 조회 시 조회수 증가</h1>
   *
   * @param id
   */
  public void increaseViewCount(Long id) {

    boardMapper.increaseViewCount(id);
  }

  /**
   *
   *
   * <h1>게시판 목록 조회 </h1>
   *
   * <br>
   * - 페이지 이동시에 검색 조건 저장
   *
   * @param req
   * @return ResBoardList (게시판 정보, 검색 조건 정보, 페이지 정보)
   */
  public ResBoardList getBoardList(ReqBoardList req) {

    System.out.println("BoardService.getBoardList: " + req.toString());
    Map<String, Object> searchConditions = buildSearchConditions(req);
    long totalBoardCount = boardMapper.getTotalBoardCount(searchConditions);
    addPageParams(searchConditions, totalBoardCount, req.getPage());

    List<Map<String, Object>> boards = boardMapper.getBoardList(searchConditions);

    return BoardDTOConverter.convertToResBoardList(boards, searchConditions);
  }

  private Map<String, Object> buildSearchConditions(ReqBoardList req) {

    Map<String, Object> searchConditions = new LinkedHashMap<>();

    String startDate = DateFormatUtils.toSearchFormat(req.getStartDate(), "00:00:00");
    searchConditions.put("startDate", startDate);

    String endDate = DateFormatUtils.toSearchFormat(req.getEndDate(), "23:59:59");
    searchConditions.put("endDate", endDate);

    searchConditions.put("categoryName", req.getCategoryName());

    searchConditions.put("keyword", req.getKeyword());

    return searchConditions;
  }

  public void addPageParams(Map<String, Object> searchConditions, long totalBoardCount, int page) {

    int limit = 10;
    int offset = (page - 1) * limit;
    int totalPages = (int) Math.ceil((double) totalBoardCount / 10);

    searchConditions.put("offset", offset);
    searchConditions.put("limit", limit);

    searchConditions.put("totalBoardCount", totalBoardCount);
    searchConditions.put("totalPages", totalPages);
    searchConditions.put("page", page);
  }
}
