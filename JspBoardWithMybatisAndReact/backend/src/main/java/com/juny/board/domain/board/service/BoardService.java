package com.juny.board.domain.board.service;

import com.juny.board.domain.board.entity.Attachment;
import com.juny.board.domain.board.entity.Board;
import com.juny.board.domain.board.entity.BoardImage;
import com.juny.board.domain.board.entity.vo.BoardListVO;
import com.juny.board.domain.board.entity.vo.BoardUpdateVO;
import com.juny.board.domain.board.entity.vo.PageInfoVO;
import com.juny.board.domain.board.entity.vo.SearchConditionVO;
import com.juny.board.domain.board.repository.AttachmentRepository;
import com.juny.board.domain.board.repository.BoardImageRepository;
import com.juny.board.domain.board.repository.BoardRepository;
import com.juny.board.domain.board.repository.CommentRepository;
import com.juny.board.domain.board.validator.BoardValidator;
import com.juny.board.global.Constants;
import com.juny.board.global.exception.ErrorCode;
import com.juny.board.global.exception.hierachy.board.BoardNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
   * @param board
   * @return ResBoardDetail
   */
  public Board getBoard(Board board) {

    Board savedBoard = boardRepository.findBoardDetailById(board.getId());

    if (savedBoard == null) {
      throw new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND);
    }

    return savedBoard;
  }

  /**
   *
   *
   * <h1>게시판 상세 페이지 조회 시 조회수 증가</h1>
   *
   * @param board
   */
  public void increaseViewCount(Board board) {

    boardRepository.increaseViewCount(board.getId());
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
  public BoardListVO getBoardList(SearchConditionVO req) {

    SearchConditionVO searchConditionVO =
        req.toBuilder()
            .startDate(req.getStartDate() + " 00:00:00")
            .endDate(req.getEndDate() + " 23:59:59")
            .limit(Constants.LIMIT)
            .offset((req.getPage() - 1) * Constants.LIMIT)
            .build();

    long totalBoardCount = boardRepository.getTotalBoardCount(searchConditionVO);

    List<Board> boards = boardRepository.getBoardList(searchConditionVO);

    PageInfoVO pageInfo = createPageInfo(totalBoardCount, searchConditionVO.getPage());

    return BoardListVO.builder().searchCondition(req).boards(boards).pageInfo(pageInfo).build();
  }

  private PageInfoVO createPageInfo(long totalBoardCount, int page) {

    int totalPages = (int) Math.ceil((double) totalBoardCount / 10);

    return PageInfoVO.builder()
        .page(page)
        .totalPages(totalPages)
        .totalBoardCount(totalBoardCount)
        .build();
  }

  /**
   *
   *
   * <h1>게시판 생성 </h1>
   *
   * <br>
   * - 게시판, 이미지, 첨부파일 DB에 저장
   *
   * @param board
   * @return boardId
   */
  @Transactional
  public void createBoard(Board board) {

    boardValidator.validatePasswordInputSame(board.getPassword(), board.getPasswordConfirm());

    boardRepository.saveBoard(board);

    for (var bi : board.getBoardImages()) {
      BoardImage boardImage = bi.toBuilder().boardId(board.getId()).build();
      boardImageRepository.saveBoardImage(boardImage);
    }

    for (var att : board.getAttachments()) {
      Attachment attachment = att.toBuilder().boardId(board.getId()).build();
      attachmentRepository.saveAttachment(attachment);
    }
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
   * @param board
   * @param updateVO
   */
  @Transactional
  public Board updateBoard(Board board, BoardUpdateVO updateVO) {

    boardValidator.validateBoardUpdate(board, updateVO);

    Long boardId = board.getId();

    boardRepository.updateBoard(board);

    List<BoardImage> boardImages = new ArrayList<>();
    for (var image : updateVO.getAddImageDetails()) {

      BoardImage boardImage =
          BoardImage.builder()
              .storedName(image.getStoredName())
              .storedPath(image.getStoredPath())
              .extension(image.getExtension())
              .boardId(boardId)
              .build();

      boardImageRepository.saveBoardImage(boardImage);
      boardImages.add(boardImage);
    }

    List<Attachment> attachments = new ArrayList<>();
    for (var att : updateVO.getAddAttachmentDetails()) {

      Attachment attachment =
          Attachment.builder()
              .logicalName(att.getLogicalName())
              .storedName(att.getStoredName())
              .storedPath(att.getStoredPath())
              .extension(att.getExtension())
              .size(att.getSize())
              .boardId(boardId)
              .build();

      attachmentRepository.saveAttachment(attachment);
      attachments.add(attachment);
    }

    for (var imageId : updateVO.getDeleteImageIds()) {
      boardImageRepository.deleteBoardImageById(imageId);
    }

    for (var attId : updateVO.getDeleteAttachmentIds()) {
      attachmentRepository.deleteAttachmentById(attId);
    }

    return board.toBuilder().boardImages(boardImages).attachments(attachments).build();
  }

  /**
   *
   *
   * <h1>게시판 삭제 DB 반영 </h1>
   *
   * <br>
   * - 생성의 역순으로 삭제 (댓글 -> 첨부파일 -> 이미지 -> 게시판)
   *
   * @param board
   */
  @Transactional
  public void deleteBoard(Board board) {

    boardValidator.validatePasswordMatch(board.getPassword(), board.getInputPassword());
    for (var comment : board.getComments()) {
      commentRepository.deleteCommentById(comment.getId());
    }
    for (var att : board.getAttachments()) {
      attachmentRepository.deleteAttachmentById(att.getId());
    }
    for (var image : board.getBoardImages()) {
      boardImageRepository.deleteBoardImageById(image.getId());
    }
    boardRepository.deleteBoardById(board.getId());
  }
}
