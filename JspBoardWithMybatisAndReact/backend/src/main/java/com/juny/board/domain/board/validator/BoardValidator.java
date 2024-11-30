package com.juny.board.domain.board.validator;

import com.juny.board.domain.board.entity.Board;
import com.juny.board.domain.board.entity.vo.BoardUpdateVO;
import com.juny.board.domain.board.repository.BoardRepository;
import com.juny.board.global.exception.ErrorCode;
import com.juny.board.global.exception.ErrorMessage;
import com.juny.board.global.exception.hierachy.common.PasswordNotMatchException;
import com.juny.board.global.exception.hierachy.common.RequestParameterInvalidException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BoardValidator {

  private final BoardRepository boardRepository;

  public BoardValidator(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  /**
   *
   *
   * <h1>게시판 수정 validator</h1>
   *
   * <br>
   * - 비밀번호 일치 검사<br>
   * - 게시판에 속하지 않은 파일 삭제 불가<br>
   *
   * @param updateVO
   * @param board
   */
  public void validateBoardUpdate(Board board, BoardUpdateVO updateVO) {

    validatePasswordMatch(board.getPassword(), board.getInputPassword());

    if (updateVO.getDeleteImageIds() != null && !updateVO.getDeleteImageIds().isEmpty()) {
      List<Long> boardImageIds =
          board.getBoardImages().stream().map(boardImage -> boardImage.getId()).toList();

      boolean isValid =
          updateVO.getDeleteImageIds().stream()
              .allMatch(deleteImageId -> boardImageIds.contains(deleteImageId));

      if (!isValid)
        throw new RequestParameterInvalidException(
            ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.UNLINKED_IMAGE_DELETE_MSG);
    }

    if (updateVO.getDeleteAttachmentIds() != null && !updateVO.getDeleteAttachmentIds().isEmpty()) {
      List<Long> attIds = board.getAttachments().stream().map(att -> att.getId()).toList();

      boolean isValid =
          updateVO.getDeleteAttachmentIds().stream()
              .allMatch(deleteAttachmentId -> attIds.contains(deleteAttachmentId));

      if (!isValid)
        throw new RequestParameterInvalidException(
            ErrorCode.REQUEST_PARAMETER_INVALID, ErrorMessage.UNLINKED_ATTACHMENT_DELETE_MSG);
    }
  }

  /**
   *
   *
   * <h1>입력 패스워드 DB 일치 여부 검사</h1>
   *
   * @param password
   * @param inputPassword
   */
  public void validatePasswordMatch(String password, String inputPassword) {

    if (!password.equals(inputPassword)) {
      throw new RequestParameterInvalidException(
          ErrorCode.PASSWORD_NOT_MATCH, ErrorMessage.PASSWORD_NOT_MATCH);
    }
  }

  /**
   *
   *
   * <h1>패스워드, 패스워드 확인 폼 데이터 일치 여부 검사 </h1>
   *
   * @param password
   * @param passwordConfirm
   */
  public void validatePasswordInputSame(String password, String passwordConfirm) {

    if (!password.equals(passwordConfirm)) {
      throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH);
    }
  }
}
