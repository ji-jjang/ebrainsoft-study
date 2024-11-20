package com.juny.jspboardwithmybatis.domain.board.service;

import com.juny.jspboardwithmybatis.domain.board.converter.BoardDTOConverter;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardCreate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardList;
import com.juny.jspboardwithmybatis.domain.board.entity.Attachment;
import com.juny.jspboardwithmybatis.domain.board.entity.Board;
import com.juny.jspboardwithmybatis.domain.board.entity.BoardImage;
import com.juny.jspboardwithmybatis.domain.board.mapper.AttachmentMapper;
import com.juny.jspboardwithmybatis.domain.board.mapper.BoardImageMapper;
import com.juny.jspboardwithmybatis.domain.board.mapper.BoardMapper;
import com.juny.jspboardwithmybatis.domain.utils.CategoryMapperUtils;
import com.juny.jspboardwithmybatis.domain.utils.DateFormatUtils;
import com.juny.jspboardwithmybatis.domain.utils.dto.FileDetails;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

  private final BoardMapper boardMapper;
  private final BoardImageMapper boardImageMapper;
  private final AttachmentMapper attachmentMapper;

  public BoardService(
      BoardMapper boardMapper,
      BoardImageMapper boardImageMapper,
      AttachmentMapper attachmentMapper) {
    this.boardMapper = boardMapper;
    this.boardImageMapper = boardImageMapper;
    this.attachmentMapper = attachmentMapper;
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
   * @return ResBoardList (게시판, 검색조건, 페이지, 카테고리 정보)
   */
  public ResBoardList getBoardList(ReqBoardList req) {

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

  @Transactional
  public Long createBoard(
      ReqBoardCreate req, List<FileDetails> images, List<FileDetails> attachments) {

    Long categoryId = CategoryMapperUtils.getCategoryIdByName(req.getCategoryName());

    Board board =
        new Board(
            req.getTitle(),
            req.getContent(),
            req.getPassword(),
            0,
            LocalDateTime.now(),
            req.getCreatedBy(),
            null,
            categoryId);

    boardMapper.saveBoard(board);

    Long boardId = board.getId();

    for (var image : images) {
      BoardImage boardImage =
          new BoardImage(
              image.getStoredName(), image.getStoredPath(), image.getExtension(), boardId);
      boardImageMapper.saveBoardImage(boardImage);
    }

    for (var att : attachments) {
      Attachment attachment =
          new Attachment(
              att.getLogicalName(),
              att.getStoredName(),
              att.getStoredPath(),
              att.getExtension(),
              att.getSize(),
              boardId);
      attachmentMapper.saveAttachment(attachment);
    }

    return boardId;
  }
}
