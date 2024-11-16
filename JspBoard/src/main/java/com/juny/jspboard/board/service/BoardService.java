package com.juny.jspboard.board.service;

import static java.util.stream.Collectors.toList;

import com.juny.jspboard.board.dao.AttachmentDAO;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardImageDAO;
import com.juny.jspboard.board.dao.CategoryDAO;
import com.juny.jspboard.board.dao.CommentDAO;
import com.juny.jspboard.board.dto.ReqBoardCreate;
import com.juny.jspboard.board.dto.ReqBoardDelete;
import com.juny.jspboard.board.dto.ReqBoardDetail;
import com.juny.jspboard.board.dto.ReqBoardList;
import com.juny.jspboard.board.dto.ReqBoardUpdate;
import com.juny.jspboard.board.dto.ReqBoardUpdatePre;
import com.juny.jspboard.board.dto.ReqCommentCreate;
import com.juny.jspboard.board.dto.ResAttachment;
import com.juny.jspboard.board.dto.ResBoardDetail;
import com.juny.jspboard.board.dto.ResBoardImage;
import com.juny.jspboard.board.dto.ResBoardList;
import com.juny.jspboard.board.dto.ResBoardViewList;
import com.juny.jspboard.board.dto.ResCategoryName;
import com.juny.jspboard.board.dto.ResComment;
import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.Board;
import com.juny.jspboard.board.entity.BoardImage;
import com.juny.jspboard.board.entity.Comment;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import com.juny.jspboard.utility.DriverManagerUtils;
import com.juny.jspboard.utility.FileUtils;
import com.juny.jspboard.utility.TimeFormatterUtils;
import com.juny.jspboard.utility.dto.ResFileParsing;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BoardService {

  private final BoardDAO boardDAO;
  private final BoardImageDAO boardImageDAO;
  private final BoardValidator validator;
  private final CategoryDAO categoryDAO;
  private final AttachmentDAO attachmentDAO;
  private final CommentDAO commentDAO;

  public BoardService(
    BoardDAO boardDAO,
    BoardImageDAO boardImageDAO,
    CategoryDAO categoryDAO,
    AttachmentDAO attachmentDAO,
    CommentDAO commentDAO,
    BoardValidator validator) {

    this.boardDAO = boardDAO;
    this.boardImageDAO = boardImageDAO;
    this.validator = validator;
    this.categoryDAO = categoryDAO;
    this.attachmentDAO = attachmentDAO;
    this.commentDAO = commentDAO;
  }

  private static Map<String, String> getSearchConditions(ReqBoardList req) {

    Map<String, String> searchConditions = new LinkedHashMap<>();

    if (req.startDate() != null && !req.startDate().isEmpty()) {
      searchConditions.put(Constants.START_DATE, req.startDate());
    }
    if (req.endDate() != null && !req.endDate().isEmpty()) {
      searchConditions.put(Constants.END_DATE, req.endDate());
    }
    if (req.categoryName() != null && !req.categoryName().isEmpty()) {
      searchConditions.put(Constants.CATEGORY, req.categoryName());
    }
    if (req.keyword() != null && !req.keyword().isEmpty()) {
      searchConditions.put(Constants.KEYWORD, req.keyword());
    }

    return searchConditions;
  }

  /**
   * <h1> 게시판 생성 시 카테고리 목록 가져오기 </h1>
   * @return
   */
  public List<ResCategoryName> getCategories() {

    List<String> categories = categoryDAO.getCategories();

    return categories.stream().map(ResCategoryName::new).toList();
  }

  /**
   * <h1> 게시판 생성 </h1>
   * - 게시판과 연관된 일대다 엔티티(게시판 이미지, 첨부 파일)에 대해 BATCH INSERT 적용할 수 있음.
   * @param reqBoardCreate
   * @param parts
   * @return
   * @throws ServletException
   * @throws IOException
   */
  public Long createBoard(ReqBoardCreate reqBoardCreate, Collection<Part> parts)
      throws IOException {

    validator.validateCreateBoardParams(reqBoardCreate);

    ResFileParsing files = FileUtils.parsingFiles(parts);

    Long boardId = null;
    try (Connection conn = DriverManagerUtils.getConnection()) {
      conn.setAutoCommit(false);

      try {
        Long categoryId = boardDAO.getCategoryIdByName(reqBoardCreate.category());

        Board board =
          new Board(
            reqBoardCreate.title(),
            reqBoardCreate.content(),
            reqBoardCreate.password(),
            0,
            LocalDateTime.now(),
            reqBoardCreate.createdBy(),
            null,
            categoryId);
        boardId = boardDAO.saveBoard(conn, board);


        for (var image : files.images()) {
          BoardImage boardImage = new BoardImage(image.getStoredName(), image.getStoredName(),
            image.getExtension(), boardId);
          boardDAO.saveBoardImage(conn, boardImage);
        }

        for (var attach : files.attachments()) {
          Attachment attachment =
            new Attachment(
              attach.getLogicalName(),
              attach.getLogicalPath(),
              attach.getStoredName(),
              attach.getStoredPath(),
              attach.getExtension(),
              attach.getSize(),
              boardId);
          boardDAO.saveAttachment(conn, attachment);
        }
        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw new RuntimeException(ErrorMessage.TRANSACTION_FAILED_MSG + Constants.SPACE_SIGN + e.getMessage(), e);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(ErrorMessage.DB_CONNECTION_FAILED_MSG + Constants.SPACE_SIGN + e.getMessage(), e);
    }

    return boardId;
  }

  /**
   * <h1> 게시판 삭제 전처리 </h1>
   * - 게시판 삭제 시 JSP 상세 페이지에서 첨부 파일과 이미지 파일 정보를 가져옴   *
   * @param req
   * @return void
   */
  public void preProcessDelete(HttpServletRequest req) {

    validator.validateDeleteBoard(req);
  }

  /**
   * <h1> 게시판 삭제 후처리 </h1>
   * <br>- DB 반영 (이미지 -> 첨부파일 -> 댓글 -> 게시글 -> 파일 경로 삭제)
   * <br>- 트랜잭션이 성공했다면 파일을 지움
   * @param reqBoardDelete
   */
  public void deleteBoard(ReqBoardDelete reqBoardDelete) {

    validator.validateDeleteExecutionBoard(reqBoardDelete);

    List<String> imagePaths = null;
    List<String> attachmentPaths = null;

    try (Connection conn = DriverManagerUtils.getConnection()) {
      conn.setAutoCommit(false);

      try {
        imagePaths = boardDAO.findImagePathsByBoardId(conn, reqBoardDelete.boardId());
        attachmentPaths = boardDAO.findAttachmentPathsByBoardId(conn, reqBoardDelete.boardId());

        boardDAO.deleteImagesByBoardId(conn, reqBoardDelete.boardId());
        boardDAO.deleteAttachmentsByBoardId(conn, reqBoardDelete.boardId());
        boardDAO.deleteCommentsByBoardId(conn, reqBoardDelete.boardId());
        boardDAO.deleteBoard(conn, reqBoardDelete.boardId());

        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw new RuntimeException(ErrorMessage.TRANSACTION_FAILED_MSG + e.getMessage(), e);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    deleteFiles(imagePaths);
    deleteFiles(attachmentPaths);
  }

  private void deleteFiles(List<String> paths) {
    for (var path : paths) {
      File file = new File(path);
      if (file.exists()) {
        file.delete();
      }
    }
  }

  /**
   * <h1> 게시판 상세 조회 </h1>
   * <br>- 게시판 엔티티 조회 후 CategoryID로 Category 이름 조회
   * <br>- 게시판 이미지, 게시판 첨부파일, 댓글 추가로 조회
   * @param req
   * @return ResBoardDetail
   */
  public ResBoardDetail getBoardDetail(ReqBoardDetail req) {

    validator.validateBoardDetailParams(req);

    try (Connection conn = DriverManagerUtils.getConnection()) {
      conn.setAutoCommit(false);

      try {
        boardDAO.increaseViewCount(conn, req.boardId());

        Board board = boardDAO.getBoardDetail(conn, req.boardId());
        String categoryName = categoryDAO.getCategoryNameByCategoryId(conn,
          board.getCategoryId());
        List<BoardImage> boardImages = boardImageDAO.getBoardImages(conn, req.boardId());
        List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(conn, req.boardId());
        List<Comment> comments = commentDAO.getComments(conn, req.boardId());

        conn.commit();

        String updatedAt = board.getUpdatedAt() != null
          ? TimeFormatterUtils.datetimeToString(board.getUpdatedAt())
          : "-";

        return new ResBoardDetail(
          board.getId(),
          board.getTitle(),
          board.getContent(),
          board.getViewCount(),
          board.getCreatedAt().toString(),
          board.getCreatedBy(),
          updatedAt,
          categoryName,
          boardImages.stream().map(img -> new ResBoardImage(img.getId(), img.getStoredName(), img.getStoredPath(), img.getExtension())).toList(),
          attachments.stream().map(att -> new ResAttachment(att.getId(), att.getLogicalName(), att.getLogicalPath(), att.getStoredName(), att.getStoredPath(), att.getExtension())).toList(),
          comments.stream().map(cmt -> new ResComment(cmt.getId(), cmt.getContent(), cmt.getCreatedAt().toString(), cmt.getCreatedBy())).toList()
        );
      } catch (SQLException e) {
        conn.rollback();
        throw new RuntimeException(ErrorMessage.TRANSACTION_FAILED_MSG + e.getMessage(), e);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * <h1> 게시판 조회는 1. 단순 목록 조회와 2. 검색 조건 파라미터 목록 조회</h1>
   * - 페이지 이동 시에도 페이지 파라미터와 같이 검색어 파라미터 저장
   * @param req
   * @return ResBoardList
   */
  public ResBoardList getBoardList(ReqBoardList req) {

    validator.validateBoardListParams(req.method());

    Map<String, String> searchConditions = getSearchConditions(req);

    int totals = searchConditions.isEmpty() ?
      boardDAO.getTotals() :
      boardDAO.getTotalsWithSearchConditions(searchConditions);

    int totalPages = (int) Math.ceil((double) totals / Constants.BOARD_LIST_PAGE_SIZE);

    List<Board> boards = searchConditions.isEmpty() ?
      boardDAO.getBoardList(req.page()) :
      boardDAO.getBoardSearchList(req.page(), searchConditions);

    List<ResBoardViewList> boardViewList = boards.stream()
      .map(board -> {
        String categoryName = categoryDAO.getCategoryNameByCategoryId(board.getCategoryId());
        boolean hasAttachment = attachmentDAO.existsAttachment(board.getId());

        String updatedAt = board.getUpdatedAt() != null
          ? TimeFormatterUtils.datetimeToString(board.getUpdatedAt())
          : "-";

        return new ResBoardViewList(
          board.getId(),
          handleLongTitle(board.getTitle()),
          board.getViewCount(),
          TimeFormatterUtils.datetimeToString(board.getCreatedAt()),
          board.getCreatedBy(),
          updatedAt,
          categoryName,
          hasAttachment
        );
      })
      .collect(toList());

    List<String> categories = categoryDAO.getCategories();

    String defaultStartDate = TimeFormatterUtils.getDefaultStartDate();
    String defaultEndDate = TimeFormatterUtils.getDefaultEndDate();

    return new ResBoardList(searchConditions, totals, totalPages, boardViewList, categories, defaultStartDate, defaultEndDate);
  }

  private String handleLongTitle(String title) {
    return title.length() > Constants.TITLE_MAX_LENGTH
        ? title.substring(0, Constants.TITLE_MAX_LENGTH) + "..."
        : title;
  }

  public ResBoardDetail preProcessModify(ReqBoardUpdatePre req) {

    validator.validateModifyBoardServlet(req);

    Board board = boardDAO.getBoardDetail(req.boardId());
    String categoryName = categoryDAO.getCategoryNameByCategoryId(board.getCategoryId());
    List<BoardImage> boardImages = boardImageDAO.getBoardImages(req.boardId());
    List<Attachment> attachments = attachmentDAO.getAttachmentsByBoardId(req.boardId());
    List<Comment> comments = commentDAO.getComments(req.boardId());

    String updatedAt = board.getUpdatedAt() != null
      ? TimeFormatterUtils.datetimeToString(board.getUpdatedAt())
      : "-";

    return new ResBoardDetail(
      board.getId(),
      board.getTitle(),
      board.getContent(),
      board.getViewCount(),
      board.getCreatedAt().toString(),
      board.getCreatedBy(),
      updatedAt,
      categoryName,
      boardImages.stream().map(img -> new ResBoardImage(img.getId(), img.getStoredName(), img.getStoredPath(), img.getExtension())).toList(),
      attachments.stream().map(att -> new ResAttachment(att.getId(), att.getLogicalName(), att.getLogicalPath(), att.getStoredName(), att.getStoredPath(), att.getExtension())).toList(),
      comments.stream().map(cmt -> new ResComment(cmt.getId(), cmt.getContent(), cmt.getCreatedAt().toString(), cmt.getCreatedBy())).toList()
    );
  }

  public void modifyBoard(ReqBoardUpdate reqBoardUpdate) {

    validator.validateProcessModifyBoard(reqBoardUpdate);

    Board board = new Board(
      reqBoardUpdate.boardId(),
      reqBoardUpdate.title(),
      reqBoardUpdate.content(),
      reqBoardUpdate.password(),
      null,
      null,
      reqBoardUpdate.createdBy(),
      LocalDateTime.now(),
      null
    );

    try (Connection conn = DriverManagerUtils.getConnection()) {
      conn.setAutoCommit(false);

      try {
        boardDAO.updateBoard(conn, board);

        processBoardImages(conn, reqBoardUpdate.boardId(), reqBoardUpdate.boardImages());

        processAttachments(conn, reqBoardUpdate.boardId(), reqBoardUpdate.attachments());

        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw new RuntimeException(ErrorMessage.TRANSACTION_FAILED_MSG);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private void processBoardImages(Connection conn, Long boardId, List<BoardImage> boardImages) {

    List<BoardImage> existingImages = boardImageDAO.getBoardImages(conn, boardId);

    List<BoardImage> imagesToDelete =
        existingImages.stream()
            .filter(
                existing ->
                    boardImages.stream()
                        .noneMatch(
                            newImage -> newImage.getStoredName().equals(existing.getStoredName())))
            .toList();

    for (var image : imagesToDelete) {
      File file = new File(image.getStoredPath() + image.getStoredName() + image.getExtension());
      if (file.exists()) {
        file.delete();
      }
      boardImageDAO.deleteBoardImageById(conn, image.getId());
    }

    List<BoardImage> imagesToAdd =
        boardImages.stream()
            .filter(
                newImage ->
                    existingImages.stream()
                        .noneMatch(
                            existing -> existing.getStoredName().equals(newImage.getStoredName())))
            .map(
                newImage ->
                    new BoardImage(
                        newImage.getStoredName(),
                        newImage.getStoredPath(),
                        newImage.getExtension(),
                        boardId))
            .toList();

    for (BoardImage image : imagesToAdd) {
      boardImageDAO.saveBoardImage(conn, image);
    }
  }

  private void processAttachments(Connection conn, Long boardId, List<Attachment> attachments) {
    List<Attachment> existingAttachments = attachmentDAO.getAttachmentsByBoardId(conn, boardId);

    List<Attachment> attachmentsToDelete = existingAttachments.stream()
      .filter(existing -> attachments.stream().noneMatch(newAttachment -> newAttachment.getStoredName().equals(existing.getStoredName())))
      .toList();

    for (var attachment : attachmentsToDelete) {
      File file = new File(attachment.getStoredPath() + attachment.getStoredName() + attachment.getExtension());
      if (file.exists()) {
        file.delete();
      }
      attachmentDAO.deleteAttachmentById(conn, attachment.getId());
    }

    List<Attachment> attachmentsToAdd =
        attachments.stream()
            .filter(
                newAttachment ->
                    existingAttachments.stream()
                        .noneMatch(
                            existing ->
                                existing.getStoredName().equals(newAttachment.getStoredName())))
            .map(
                attachment ->
                    new Attachment(
                        attachment.getLogicalName(),
                        attachment.getLogicalPath(),
                        attachment.getStoredName(),
                        attachment.getStoredPath(),
                        attachment.getExtension(),
                        attachment.getSize(),
                        boardId))
            .toList();

    for (var attachment : attachmentsToAdd) {
      attachmentDAO.saveAttachment(conn, attachment);
    }
  }

  public void createComment(ReqCommentCreate req) {

    validator.validateCreateCommentParams(req);

    Comment comment =
        new Comment(req.content(), req.password(), LocalDateTime.now(), req.name(), req.boardId());
    commentDAO.createComment(comment);
  }
}
