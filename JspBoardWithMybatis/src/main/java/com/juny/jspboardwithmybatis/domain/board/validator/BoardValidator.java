package com.juny.jspboardwithmybatis.domain.board.validator;

import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreCreate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreUpdate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqForm;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.mapper.BoardMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BoardValidator {

  private final BoardMapper boardMapper;
  private final int MIN_AUTHOR_LENGTH = 3;
  private final int MAX_AUTHOR_LENGTH = 4;
  private final int MIN_PASSWORD_LENGTH = 4;
  private final int MAX_PASSWORD_LENGTH = 15;
  private final int MIN_TITLE_LENGTH = 4;
  private final int MAX_TITLE_LENGTH = 99;
  private final int MIN_CONTENT_LENGTH = 4;
  private final int MAX_CONTENT_LENGTH = 1999;

  public BoardValidator(BoardMapper boardMapper) {
    this.boardMapper = boardMapper;
  }

  /**
   *
   *
   * <h1>게시판 목록 조회 validator</h1>
   *
   * <br>
   * - 요청 파라미터 null, empty 검사
   *
   * @param req
   */
  public void validateBoardList(ReqBoardList req) {

    if (req == null) {
      throw new RuntimeException("ReqBoardList is null");
    }

    if (req.getPage() < 1) {
      throw new RuntimeException("ReqBoardList page is less than 1");
    }
  }

  /**
   *
   *
   * <h1>게시판 생성 validator</h1>
   *
   * <br>
   * - 요청 파라미터 null, empty 검사<br>
   * - 패스워드 일치 검사<br>
   * - 요청 파라미터 입력 폼 검사 (작성자 길이, 패스워드 길이, 제목, 내용 등 길이)
   *
   * @param req
   */
  public void validateBoardCreate(ReqBoardPreCreate req) {

    if (req == null) {
      throw new RuntimeException("ReqBoardPreCreate is null");
    }

    if (req.getCategoryName() == null || req.getCategoryName().isEmpty()) {
      throw new RuntimeException("ReqBoardPreCreate categoryName is null or empty");
    }

    if (req.getCreatedBy() == null || req.getCreatedBy().isEmpty()) {
      throw new RuntimeException("ReqBoardPreCreate createdBy is null or empty");
    }

    if (req.getPassword() == null || req.getPassword().isEmpty()) {
      throw new RuntimeException("ReqBoardPreCreate password is null or empty");
    }

    if (req.getPasswordConfirm() == null || req.getPasswordConfirm().isEmpty()) {
      throw new RuntimeException("ReqBoardPreCreate passwordConfirm is null or empty");
    }

    if (req.getTitle() == null || req.getTitle().isEmpty()) {
      throw new RuntimeException("ReqBoardPreCreate title is null or empty");
    }

    if (req.getContent() == null || req.getContent().isEmpty()) {
      throw new RuntimeException("ReqBoardPreCreate content is null or empty");
    }

    if (!req.getPassword().equals(req.getPasswordConfirm())) {
      throw new RuntimeException("ReqBoardPreCreate password and passwordConfirm are not equal");
    }

    validateBoardFormParams(req);
  }

  private void validateBoardFormParams(ReqForm req) {

    if (req.getCreatedBy().length() < MIN_AUTHOR_LENGTH) {
      throw new RuntimeException("Min author length is " + MIN_AUTHOR_LENGTH);
    }

    if (req.getCreatedBy().length() > MAX_AUTHOR_LENGTH) {
      throw new RuntimeException("Max author length is " + MAX_AUTHOR_LENGTH);
    }

    if (req.getPassword().length() < MIN_PASSWORD_LENGTH) {
      throw new RuntimeException("Min password length is " + MIN_PASSWORD_LENGTH);
    }

    if (req.getPassword().length() > MAX_PASSWORD_LENGTH) {
      throw new RuntimeException("Max password length is " + MAX_PASSWORD_LENGTH);
    }

    if (req.getTitle().length() < MIN_TITLE_LENGTH) {
      throw new RuntimeException("Min title length is " + MIN_TITLE_LENGTH);
    }

    if (req.getTitle().length() > MAX_TITLE_LENGTH) {
      throw new RuntimeException("Max title length is " + MAX_TITLE_LENGTH);
    }

    if (req.getContent().length() < MIN_CONTENT_LENGTH) {
      throw new RuntimeException("Min content length is " + MIN_CONTENT_LENGTH);
    }

    if (req.getContent().length() > MAX_CONTENT_LENGTH) {
      throw new RuntimeException("Max content length is " + MAX_CONTENT_LENGTH);
    }
  }

  /**
   *
   *
   * <h1>게시판 수정 validator</h1>
   *
   * <br>
   * - 요청 파라미터 null, empty 검사<br>
   * - DB 비밀번호 조회 후 일치 검사<br>
   * - 게시판에 속하지 않은 파일 삭제 불가<br>
   *
   * @param boardId
   * @param req
   * @param board
   */
  public void validateBoardUpdate(Long boardId, ReqBoardPreUpdate req, ResBoardDetail board) {

    if (req.getTitle() == null || req.getTitle().isEmpty()) {
      throw new RuntimeException("ReqBoardPreUpdate title is null or empty");
    }

    if (req.getContent() == null || req.getContent().isEmpty()) {
      throw new RuntimeException("ReqBoardPreUpdate content is null or empty");
    }

    if (req.getPassword() == null || req.getPassword().isEmpty()) {
      throw new RuntimeException("ReqBoardPreUpdate password is null or empty");
    }

    if (req.getCreatedBy() == null || req.getCreatedBy().isEmpty()) {
      throw new RuntimeException("ReqBoardPreUpdate createdBy is null or empty");
    }

    if (!req.getPassword().equals(boardMapper.getBoardPassword(boardId))) {
      throw new RuntimeException("Board password not match");
    }

    validateBoardFormParams(req);

    if (req.getDeleteImageIds() != null && !req.getDeleteImageIds().isEmpty()) {
      List<Long> boardImageIds =
          board.getBoardImages().stream().map(boardImage -> boardImage.getId()).toList();

      boolean isValid =
          req.getDeleteImageIds().stream()
              .allMatch(deleteImageId -> boardImageIds.contains(deleteImageId));

      if (!isValid)
        throw new RuntimeException("Images not associated with the board cannot be deleted.");
    }

    if (req.getDeleteAttachmentIds() != null && !req.getDeleteAttachmentIds().isEmpty()) {
      List<Long> attIds = board.getAttachments().stream().map(att -> att.getId()).toList();

      boolean isValid =
          req.getDeleteImageIds().stream()
              .allMatch(deleteImageId -> attIds.contains(deleteImageId));
      if (!isValid)
        throw new RuntimeException("Attachments not associated with the board cannot be deleted.");
    }
  }

  /**
   *
   *
   * <h1>게시판 삭제 validator</h1>
   *
   * <br>
   * - 입력 파라미터 null, empty 검사<br>
   * - 패스워드 길이 검사<br>
   * - 패스워드 일치여부 검사
   *
   * @param boardId
   * @param password
   */
  public void validateBoardDelete(Long boardId, String password) {

    if (password == null || password.isEmpty()) {
      throw new RuntimeException("Board password is null or empty");
    }

    if (password.length() < MIN_PASSWORD_LENGTH) {
      throw new RuntimeException("Min password length is " + MIN_PASSWORD_LENGTH);
    }

    if (password.length() > MAX_PASSWORD_LENGTH) {
      throw new RuntimeException("Max password length is " + MAX_PASSWORD_LENGTH);
    }

    if (!password.equals(boardMapper.getBoardPassword(boardId))) {
      throw new RuntimeException("Board password not match");
    }
  }
}
