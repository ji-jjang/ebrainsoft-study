package com.juny.jspboardwithmybatis.domain.board.validator;

import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreCreate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreUpdate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqForm;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.mapper.BoardMapper;
import com.juny.jspboardwithmybatis.global.exception.ErrorCode;
import com.juny.jspboardwithmybatis.global.exception.ErrorMessage;
import com.juny.jspboardwithmybatis.global.exception.hierachy.common.PasswordNotMatchException;
import com.juny.jspboardwithmybatis.global.exception.hierachy.common.RequestParameterInvalidException;
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
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getPage() < 1) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.INVALID_PAGE_PARAMETER);
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
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getCategoryName() == null || req.getCategoryName().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getCreatedBy() == null || req.getCreatedBy().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getPassword() == null || req.getPassword().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getPasswordConfirm() == null || req.getPasswordConfirm().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getTitle() == null || req.getTitle().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getContent() == null || req.getContent().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (!req.getPassword().equals(req.getPasswordConfirm())) {
      throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    validateBoardFormParams(req);
  }

  private void validateBoardFormParams(ReqForm req) {

    if (req.getCreatedBy().length() < MIN_AUTHOR_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "min_author_length: " + MIN_AUTHOR_LENGTH);
    }

    if (req.getCreatedBy().length() > MAX_AUTHOR_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "max_author_length: " + MAX_AUTHOR_LENGTH);
    }

    if (req.getPassword().length() < MIN_PASSWORD_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "min_password_length: " + MIN_PASSWORD_LENGTH);
    }

    if (req.getPassword().length() > MAX_PASSWORD_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "max_password_length: " + MAX_PASSWORD_LENGTH);
    }

    if (req.getTitle().length() < MIN_TITLE_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "min_title_length: " + MIN_TITLE_LENGTH);
    }

    if (req.getTitle().length() > MAX_TITLE_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "max_title_length: " + MAX_TITLE_LENGTH);
    }

    if (req.getContent().length() < MIN_CONTENT_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "min_content_length: " + MIN_CONTENT_LENGTH);
    }

    if (req.getContent().length() > MAX_CONTENT_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "max_content_length: " + MAX_CONTENT_LENGTH);
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
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getContent() == null || req.getContent().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getPassword() == null || req.getPassword().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (req.getCreatedBy() == null || req.getCreatedBy().isEmpty()) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (!req.getPassword().equals(boardMapper.getBoardPassword(boardId))) {
      throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    validateBoardFormParams(req);

    if (req.getDeleteImageIds() != null && !req.getDeleteImageIds().isEmpty()) {
      List<Long> boardImageIds =
          board.getBoardImages().stream().map(boardImage -> boardImage.getId()).toList();

      boolean isValid =
          req.getDeleteImageIds().stream()
              .allMatch(deleteImageId -> boardImageIds.contains(deleteImageId));

      if (!isValid)
        throw new RequestParameterInvalidException(
            ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.UNLINKED_IMAGE_DELETE_MSG);
    }

    if (req.getDeleteAttachmentIds() != null && !req.getDeleteAttachmentIds().isEmpty()) {
      List<Long> attIds = board.getAttachments().stream().map(att -> att.getId()).toList();

      boolean isValid =
          req.getDeleteImageIds().stream()
              .allMatch(deleteImageId -> attIds.contains(deleteImageId));
      if (!isValid)
        throw new RequestParameterInvalidException(
            ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.UNLINKED_ATTACHMENT_DELETE_MSG);
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
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.PARAMETER_NULL_OR_EMPTY);
    }

    if (password.length() < MIN_PASSWORD_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "Min_password_length: " + MIN_PASSWORD_LENGTH);
    }

    if (password.length() > MAX_PASSWORD_LENGTH) {
      throw new RequestParameterInvalidException(
          ErrorCode.REQUEST_PARAMETER_INVALID, "Max_password_length: " + MAX_PASSWORD_LENGTH);
    }

    if (!password.equals(boardMapper.getBoardPassword(boardId))) {
      throw new RequestParameterInvalidException(
          ErrorCode.PASSWORD_NOT_MATCH, ErrorMessage.PASSWORD_NOT_MATCH);
    }
  }
}
