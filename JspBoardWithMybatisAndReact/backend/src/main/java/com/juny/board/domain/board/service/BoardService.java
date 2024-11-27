package com.juny.board.domain.board.service;

import com.juny.board.domain.board.converter.BoardDTOConverter;
import com.juny.board.domain.board.dto.ReqBoardCreate;
import com.juny.board.domain.board.dto.ReqBoardDelete;
import com.juny.board.domain.board.dto.ReqBoardList;
import com.juny.board.domain.board.dto.ReqBoardPreCreate;
import com.juny.board.domain.board.dto.ReqBoardPreUpdate;
import com.juny.board.domain.board.dto.ReqBoardUpdate;
import com.juny.board.domain.board.dto.ResBoardDetail;
import com.juny.board.domain.board.dto.ResBoardList;
import com.juny.board.domain.board.entity.Attachment;
import com.juny.board.domain.board.entity.Board;
import com.juny.board.domain.board.entity.BoardImage;
import com.juny.board.domain.board.repository.AttachmentRepository;
import com.juny.board.domain.board.repository.BoardImageRepository;
import com.juny.board.domain.board.repository.BoardRepository;
import com.juny.board.domain.board.repository.CommentRepository;
import com.juny.board.domain.board.validator.BoardValidator;
import com.juny.board.domain.utils.CategoryMapperUtils;
import com.juny.board.domain.utils.DateFormatUtils;
import com.juny.board.domain.utils.FileUtils;
import com.juny.board.domain.utils.dto.FileDetails;
import com.juny.board.global.exception.ErrorCode;
import com.juny.board.global.exception.hierachy.board.BoardNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final BoardImageRepository boardImageRepository;
  private final AttachmentRepository attachmentRepository;
  private final CommentRepository commentRepository;
  private final BoardValidator boardValidator;

  /**
   *
   *
   * <h1>게시판 상세 페이지 조회 </h1>
   *
   * @param id
   * @return ResBoardDetail
   */
  public ResBoardDetail getBoard(Long id) {

    Map<String, Object> board = boardRepository.findBoardDetailById(id);

    if (board == null) {
      throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
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

    boardRepository.increaseViewCount(id);
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

    boardValidator.validateBoardList(req);

    Map<String, Object> searchConditions = buildSearchConditions(req);
    long totalBoardCount = boardRepository.getTotalBoardCount(searchConditions);
    addPageParams(searchConditions, totalBoardCount, req.getPage());

    List<Map<String, Object>> boards = boardRepository.getBoardList(searchConditions);

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

  private void addPageParams(Map<String, Object> searchConditions, long totalBoardCount, int page) {

    int limit = 10;
    int offset = (page - 1) * limit;
    int totalPages = (int) Math.ceil((double) totalBoardCount / 10);

    searchConditions.put("offset", offset);
    searchConditions.put("limit", limit);

    searchConditions.put("totalBoardCount", totalBoardCount);
    searchConditions.put("totalPages", totalPages);
    searchConditions.put("page", page);
  }

  /**
   *
   *
   * <h1>게시판 생성 전처리</h1>
   *
   * @param reqBoardPreCreate
   * @return ReqBoardCreate (파일시스템에 저장할 이미지, 첨부파일 경로 추가)
   */
  public ReqBoardCreate preProcessCreate(ReqBoardPreCreate reqBoardPreCreate) {

    boardValidator.validateBoardCreate(reqBoardPreCreate);

    List<FileDetails> imageDetails =
        FileUtils.parseFileDetails(reqBoardPreCreate.getImages(), "images");

    List<FileDetails> attachmentsDetails =
        FileUtils.parseFileDetails(reqBoardPreCreate.getAttachments(), "attachments");

    return new ReqBoardCreate(
        reqBoardPreCreate.getCategoryName(),
        reqBoardPreCreate.getCreatedBy(),
        reqBoardPreCreate.getPassword(),
        reqBoardPreCreate.getPasswordConfirm(),
        reqBoardPreCreate.getTitle(),
        reqBoardPreCreate.getContent(),
        reqBoardPreCreate.getImages(),
        reqBoardPreCreate.getAttachments(),
        imageDetails,
        attachmentsDetails);
  }

  /**
   *
   *
   * <h1>게시판 생성 </h1>
   *
   * <br>
   * - 게시판, 이미지, 첨부파일 DB에 저장
   *
   * @param req
   * @return boardId
   */
  @Transactional
  public Long createBoard(ReqBoardCreate req) {

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

    boardRepository.saveBoard(board);

    Long boardId = board.getId();

    for (var image : req.getImageDetails()) {
      BoardImage boardImage =
          new BoardImage(
              image.getStoredName(), image.getStoredPath(), image.getExtension(), boardId);
      boardImageRepository.saveBoardImage(boardImage);
    }

    for (var att : req.getAttachmentDetails()) {
      Attachment attachment =
          new Attachment(
              att.getLogicalName(),
              att.getStoredName(),
              att.getStoredPath(),
              att.getExtension(),
              att.getSize(),
              boardId);
      attachmentRepository.saveAttachment(attachment);
    }

    return boardId;
  }

  /**
   *
   *
   * <h1>게시판 수정 전처리 </h1>
   *
   * <p>- 전처리 과정: 추가할 파일 상세 정보와 제거할 파일 경로 DTO 추가<br>
   *
   * @param id
   * @param board
   * @param reqBoardPreUpdate
   * @return reqBoardUpdate
   */
  public ReqBoardUpdate preProcessUpdate(
      Long id, ResBoardDetail board, ReqBoardPreUpdate reqBoardPreUpdate) {

    boardValidator.validateBoardUpdate(id, reqBoardPreUpdate, board);

    List<String> deleteFilePaths =
        Stream.concat(
                board.getBoardImages().stream()
                    .filter(
                        boardImage ->
                            reqBoardPreUpdate.getDeleteImageIds().contains(boardImage.getId()))
                    .map(
                        boardImage -> {
                          String delimiter = boardImage.getExtension().isEmpty() ? "" : ".";
                          return boardImage.getStoredPath()
                              + boardImage.getStoredName()
                              + delimiter
                              + boardImage.getExtension();
                        }),
                board.getAttachments().stream()
                    .filter(
                        attachment ->
                            reqBoardPreUpdate.getDeleteAttachmentIds().contains(attachment.getId()))
                    .map(
                        attachment -> {
                          String delimiter = attachment.getExtension().isEmpty() ? "" : ".";
                          return attachment.getStoredPath()
                              + attachment.getStoredName()
                              + delimiter
                              + attachment.getExtension();
                        }))
            .toList();

    List<FileDetails> imageDetails =
        FileUtils.parseFileDetails(reqBoardPreUpdate.getImages(), "images");
    List<FileDetails> attachmentDetails =
        FileUtils.parseFileDetails(reqBoardPreUpdate.getAttachments(), "attachments");

    return new ReqBoardUpdate(
        reqBoardPreUpdate.getTitle(),
        reqBoardPreUpdate.getContent(),
        reqBoardPreUpdate.getPassword(),
        reqBoardPreUpdate.getCreatedBy(),
        reqBoardPreUpdate.getDeleteImageIds(),
        reqBoardPreUpdate.getDeleteAttachmentIds(),
        reqBoardPreUpdate.getImages(),
        reqBoardPreUpdate.getAttachments(),
        imageDetails,
        attachmentDetails,
        deleteFilePaths);
  }

  /**
   *
   *
   * <h1>게시판 수정 DB 반영 </h1>
   *
   * <br>
   * - 게시판 생성, 파일(이미지, 첨부파일) 추가 및 제거<br>
   * - 추후 Batch Insert 적용 가능
   *
   * @param boardId
   * @param req
   */
  @Transactional
  public void updateBoard(Long boardId, ReqBoardUpdate req) {

    Board board =
        new Board(
            boardId,
            req.getTitle(),
            req.getContent(),
            req.getPassword(),
            null,
            null,
            req.getCreatedBy(),
            LocalDateTime.now(),
            null);

    boardRepository.updateBoard(board);

    for (var image : req.getImageDetails()) {
      BoardImage boardImage =
          new BoardImage(
              image.getStoredName(), image.getStoredPath(), image.getExtension(), boardId);

      boardImageRepository.saveBoardImage(boardImage);
    }

    for (var att : req.getAttachmentDetails()) {
      Attachment attachment =
          new Attachment(
              att.getLogicalName(),
              att.getStoredName(),
              att.getStoredPath(),
              att.getExtension(),
              att.getSize(),
              boardId);

      attachmentRepository.saveAttachment(attachment);
    }

    for (var imageId : req.getDeleteImageIds()) {
      boardImageRepository.deleteBoardImageById(imageId);
    }

    for (var attId : req.getDeleteAttachmentIds()) {
      attachmentRepository.deleteAttachmentById(attId);
    }
  }

  /**
   *
   *
   * <h1>게시판 삭제 전처리</h1>
   *
   * <br>
   * - 삭제할 파일(이미지, 첨부파일) 경로 파싱
   *
   * @param boardId
   * @param password
   * @param board
   * @return
   */
  public ReqBoardDelete preProcessDelete(Long boardId, String password, ResBoardDetail board) {

    boardValidator.validateBoardDelete(boardId, password);

    List<String> deleteFilePaths =
        Stream.concat(
                board.getBoardImages().stream()
                    .map(
                        boardImage -> {
                          String delimiter = boardImage.getExtension().isEmpty() ? "" : ".";
                          return boardImage.getStoredPath()
                              + boardImage.getStoredName()
                              + delimiter
                              + boardImage.getExtension();
                        }),
                board.getAttachments().stream()
                    .map(
                        attachment -> {
                          String delimiter = attachment.getExtension().isEmpty() ? "" : ".";
                          return attachment.getStoredPath()
                              + attachment.getStoredName()
                              + delimiter
                              + attachment.getExtension();
                        }))
            .toList();

    return new ReqBoardDelete(
        boardId,
        password,
        board.getBoardImages(),
        board.getAttachments(),
        board.getComments(),
        deleteFilePaths);
  }

  /**
   *
   *
   * <h1>게시판 삭제 DB 반영 </h1>
   *
   * <br>
   * - 생성의 역순으로 삭제 (댓글 -> 첨부파일 -> 이미지 -> 게시판)
   *
   * @param reqBoardDelete
   */
  @Transactional
  public void deleteBoard(ReqBoardDelete reqBoardDelete) {

    for (var comment : reqBoardDelete.getComments()) {
      commentRepository.deleteCommentById(comment.getId());
    }
    for (var att : reqBoardDelete.getAttachments()) {
      attachmentRepository.deleteAttachmentById(att.getId());
    }
    for (var image : reqBoardDelete.getBoardImages()) {
      boardImageRepository.deleteBoardImageById(image.getId());
    }
    boardRepository.deleteBoardById(reqBoardDelete.getBoardId());
  }
}
