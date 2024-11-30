package com.juny.board.domain.board.controller;

import com.juny.board.domain.board.dto.ReqBoardCreate;
import com.juny.board.domain.board.dto.ReqBoardDelete;
import com.juny.board.domain.board.dto.ReqBoardList;
import com.juny.board.domain.board.dto.ReqBoardUpdate;
import com.juny.board.domain.board.dto.ResBoardDetail;
import com.juny.board.domain.board.dto.ResBoardList;
import com.juny.board.domain.board.entity.Board;
import com.juny.board.domain.board.entity.vo.BoardDeleteVO;
import com.juny.board.domain.board.entity.vo.BoardListVO;
import com.juny.board.domain.board.entity.vo.BoardUpdateVO;
import com.juny.board.domain.board.entity.vo.SearchConditionVO;
import com.juny.board.domain.board.mapper.BoardMapper;
import com.juny.board.domain.board.service.BoardService;
import com.juny.board.domain.utils.FileService;
import com.juny.board.domain.utils.dto.FileDetails;
import com.juny.board.global.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BoardController {

  private final BoardService boardService;
  private final BoardMapper boardMapper;
  private final FileService fileService;

  public BoardController(
      BoardService boardService, BoardMapper boardMapper, FileService fileService) {
    this.boardService = boardService;
    this.boardMapper = boardMapper;
    this.fileService = fileService;
  }

  /**
   *
   *
   * <h1>게시판 상세 페이지 조회 </h1
   * <br>
   *
   * - 조회수 증가와 상세 페이지 조회를 같은 트랜잭션으로 묶을 필요 없음 <br>
   * - 오히려 비동기 처리할 것
   *
   * @param id
   * @return ResBoardDetail
   */
  @GetMapping("/v1/boards/{id}")
  public ResBoardDetail getBoard(@PathVariable Long id) {

    Board board = Board.builder().id(id).build();

    boardService.increaseViewCount(board);

    Board savedBoard = boardService.getBoard(board);

    return boardMapper.toResBoardDetail(savedBoard);
  }

  /**
   *
   *
   * <h1>게시판 목록 페이지 조회 </h1>
   *
   * <br>
   * - 검색 조건을 유지하며, 페이지 이동 할 수 있어야 함<br>
   * - 검색 조건이 세션에 있다면, 갱신
   *
   * @return ResBoardList (게시판 정보, 검색 조건, 페이지 파라미터)
   */
  @GetMapping("/v1/boards")
  public ResBoardList getBoards(@ModelAttribute ReqBoardList req) {

    SearchConditionVO searchConditionVO =
        SearchConditionVO.builder()
            .startDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(req.getStartDate()))
            .endDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(req.getEndDate()))
            .categoryName(req.getCategoryName())
            .keyword(req.getKeyword())
            .page(req.getPage())
            .build();

    BoardListVO boardListVO = boardService.getBoardList(searchConditionVO);

    return boardMapper.toResBoardList(boardListVO);
  }

  /**
   *
   *
   * <h1>게시판 생성 </h1>
   *
   * <br>
   * - 1. 생성 전처리(저장할 파일 정보 파싱)<br>
   * - 2. 서비스 트랜잭션으로 게시판 생성(게시판, 이미지, 첨부파일)<br>
   * - 3. 트랜잭션 성공 시 파일 시스템에 저장
   *
   * @return void
   */
  @PostMapping("/v1/boards")
  public void createBoard(@Validated @ModelAttribute ReqBoardCreate req) {

    Board board = boardMapper.toBoardEntity(req, fileService);

    boardService.createBoard(board);

    fileService.saveFile(req.getImages(), board.getBoardImages());
    fileService.saveFile(req.getAttachments(), board.getAttachments());
  }

  /**
   *
   *
   * <h1>게시판 수정 </h1>
   *
   * <br>
   * - 요청 검증 위해 먼저 게시판 상세 조회 쿼리 실행<br>
   * - 전처리 과정: 추가할 파일 상세 정보와 제거할 파일 경로 VO 추가<br>
   * - 트랜잭션 성공 시 파일 시스템에 파일 추가 및 삭제<br>
   *
   * @return void
   */
  @PutMapping("/v1/boards/{id}")
  public void updateBoard(@Validated @PathVariable Long id, @ModelAttribute ReqBoardUpdate req) {

    Board savedBoard = boardService.getBoard(Board.builder().id(id).build());

    Board board =
        savedBoard.toBuilder()
            .title(req.getTitle())
            .content(req.getContent())
            .createdBy(req.getCreatedBy())
            .inputPassword(req.getPassword())
            .updatedAt(LocalDateTime.now())
            .build();

    BoardUpdateVO boardUpdateVO = processFileChanges(board, req);

    board = boardService.updateBoard(board, boardUpdateVO);

    fileService.saveFile(req.getAddImages(), board.getBoardImages());
    fileService.saveFile(req.getAddAttachments(), board.getAttachments());
    fileService.deleteFile(boardUpdateVO.getDeleteFilePaths());
  }

  /**
   *
   *
   * <h1>삭제할 파일 경로, 추가할 파일(이미지, 첨부파일) 정보 파싱</h1>
   *
   * @param board
   * @param req
   * @return BoardUpdateVO
   */
  private BoardUpdateVO processFileChanges(Board board, ReqBoardUpdate req) {

    List<String> deleteFilePaths =
        Stream.concat(
                board.getBoardImages().stream()
                    .filter(boardImage -> req.getDeleteImageIds().contains(boardImage.getId()))
                    .map(
                        boardImage -> {
                          String delimiter =
                              boardImage.getExtension().isEmpty()
                                  ? Constants.EMPTY_SIGN
                                  : Constants.DOT_SIGN;
                          return boardImage.getStoredPath()
                              + boardImage.getStoredName()
                              + delimiter
                              + boardImage.getExtension();
                        }),
                board.getAttachments().stream()
                    .filter(attachment -> req.getDeleteAttachmentIds().contains(attachment.getId()))
                    .map(
                        attachment -> {
                          String delimiter =
                              attachment.getExtension().isEmpty()
                                  ? Constants.EMPTY_SIGN
                                  : Constants.DOT_SIGN;
                          return attachment.getStoredPath()
                              + attachment.getStoredName()
                              + delimiter
                              + attachment.getExtension();
                        }))
            .toList();

    List<FileDetails> imageDetails = fileService.parseFileDetails(req.getAddImages(), "images");
    List<FileDetails> attachmentDetails =
        fileService.parseFileDetails(req.getAddAttachments(), "attachments");

    return new BoardUpdateVO(
        req.getDeleteImageIds(),
        req.getDeleteAttachmentIds(),
        deleteFilePaths,
        imageDetails,
        attachmentDetails);
  }

  /**
   *
   *
   * <h1>게시판 삭제 </h1>
   *
   * <br>
   * - 삭제하기 전 삭제할 이미지, 첨부파일 경로 전처리<br>
   * - 트랜잭션 안에서 댓글 -> 첨부파일 -> 게시판 이미지 -> 게시판 순으로 삭제<br>
   * - 트랜잭션이 성공했다면 파일 삭제
   *
   * @param id
   * @param req (password)
   * @return void
   */
  @DeleteMapping("/v1/boards/{id}")
  public void deleteBoard(@Validated @PathVariable Long id, @RequestBody ReqBoardDelete req) {

    Board savedBoard = boardService.getBoard(Board.builder().id(id).build());

    Board board = savedBoard.toBuilder().inputPassword(req.getPassword()).build();

    BoardDeleteVO boardDeleteVO = preProcessDelete(board);

    boardService.deleteBoard(board);

    fileService.deleteFile(boardDeleteVO.getDeleteFilePaths());
  }

  /**
   *
   *
   * <h1>게시판 삭제 전처리</h1>
   *
   * <br>
   * - 삭제할 파일(이미지, 첨부파일) 경로 파싱
   *
   * @param board
   * @return BoardDeleteVO
   */
  public BoardDeleteVO preProcessDelete(Board board) {

    List<String> deleteFilePaths =
        Stream.concat(
                board.getBoardImages().stream()
                    .map(
                        boardImage -> {
                          String delimiter =
                              boardImage.getExtension().isEmpty()
                                  ? Constants.EMPTY_SIGN
                                  : Constants.DOT_SIGN;
                          return boardImage.getStoredPath()
                              + boardImage.getStoredName()
                              + delimiter
                              + boardImage.getExtension();
                        }),
                board.getAttachments().stream()
                    .map(
                        attachment -> {
                          String delimiter =
                              attachment.getExtension().isEmpty()
                                  ? Constants.EMPTY_SIGN
                                  : Constants.DOT_SIGN;
                          return attachment.getStoredPath()
                              + attachment.getStoredName()
                              + delimiter
                              + attachment.getExtension();
                        }))
            .toList();

    return new BoardDeleteVO(deleteFilePaths);
  }
}
