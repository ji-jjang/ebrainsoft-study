package com.juny.jspboard.validator;

import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardDAOImpl;
import com.juny.jspboard.board.dto.ReqBoardDelete;
import com.juny.jspboard.board.dto.ReqBoardForm;
import com.juny.jspboard.board.dto.ReqBoardUpdate;
import com.juny.jspboard.board.dto.ReqCommentCreate;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import com.juny.jspboard.board.dto.ReqBoardCreate;
import jakarta.servlet.http.HttpServletRequest;

public class BoardValidator {

  private final int MIN_AUTHOR_LENGTH = 3;
  private final int MAX_AUTHOR_LENGTH = 4;
  private final int MIN_PASSWORD_LENGTH = 4;
  private final int MAX_PASSWORD_LENGTH = 15;
  private final int MIN_TITLE_LENGTH = 4;
  private final int MAX_TITLE_LENGTH = 99;
  private final int MIN_CONTENT_LENGTH = 4;
  private final int MAX_CONTENT_LENGTH = 1999;
  private final BoardDAO boardDAO;

  public BoardValidator() {
    boardDAO = new BoardDAOImpl();
  }

  /**
   * url, localhost:8080/boards/free/list
   *
   * @param req
   */
  public void validateBoardListParams(HttpServletRequest req) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_GET, req.getMethod())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + req.getMethod());
    }
  }

  /**
   * url, localhost:http://localhost:8080/boards/free/view/{id}
   *
   * @param req
   */
  public void validateBoardDetailParams(HttpServletRequest req) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_GET, req.getMethod())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + req.getMethod());
    }
  }

  /**
   * url, /processCreateBoard
   *
   * @param req
   */
  public void validateCreateBoardParams(ReqBoardCreate req) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_POST, req.method())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + req.method());
    }

    if (!checkPasswordMatchInput(req.password(), req.passwordConfirm()))
      throw new RuntimeException(ErrorMessage.INPUT_PASSWORD_NOT_MATCH_MSG);

    validateBoardFormParams(req);
  }

  private boolean checkHttpMethodMatch(String expected, String actual) {
    return expected.equals(actual);
  }

  private boolean checkPasswordMatchInput(String password, String passwordConfirm) {
    return password.equals(passwordConfirm);
  }

  /**
   * url, /downloads
   *
   * @param req
   */
  public void validateFileDownloadServlet(HttpServletRequest req) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_GET, req.getMethod())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + req.getMethod());
    }
  }

  /**
   * url, /boards/free/modify/{id} [MODIFY FORM}
   *
   * @param req
   */
  public void validateModifyBoardServlet(HttpServletRequest req) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_GET, req.getMethod())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + req.getMethod());
    }
  }

  /**
   * url, /processModifyBoard
   *
   * @param reqBoardUpdate
   */
  public void validateProcessModifyBoard(ReqBoardUpdate reqBoardUpdate) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_POST, reqBoardUpdate.method())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + reqBoardUpdate.method());
    }

    validateBoardFormParams(reqBoardUpdate);

    if (!checkPasswordMatch(reqBoardUpdate.boardId(), reqBoardUpdate.password())) {
      throw new RuntimeException(ErrorMessage.PASSWORD_NOT_MATCH_MSG);
    }
  }

  private boolean checkPasswordMatch(Long boardId, String password) {

    String storedPassword = boardDAO.getStoredPassword(boardId);

    return storedPassword.equals(password);
  }

  private void validateBoardFormParams(ReqBoardForm req) {

    if (req.createdBy().length() < MIN_AUTHOR_LENGTH) {
      throw new RuntimeException(ErrorMessage.CREATED_BY_NAME_TOO_SHORT_MSG + req.createdBy());
    }

    if (req.createdBy().length() > MAX_AUTHOR_LENGTH) {
      throw new RuntimeException(ErrorMessage.CREATED_BY_NAME_TOO_LONG_MSG + req.createdBy());
    }

    if (req.password().length() < MIN_PASSWORD_LENGTH) {
      throw new RuntimeException(ErrorMessage.PASSWORD_TOO_SHORT_MSG + req.password());
    }

    if (req.password().length() > MAX_PASSWORD_LENGTH) {
      throw new RuntimeException(ErrorMessage.PASSWORD_TOO_LONG_MSG + req.password());
    }

    if (req.title().length() < MIN_TITLE_LENGTH) {
      throw new RuntimeException(ErrorMessage.TITLE_TOO_SHORT_MSG + req.title());
    }

    if (req.title().length() > MAX_TITLE_LENGTH) {
      throw new RuntimeException(ErrorMessage.TITLE_TOO_LONG_MSG + req.title());
    }

    if (req.content().length() < MIN_CONTENT_LENGTH) {
      throw new RuntimeException(ErrorMessage.CONTENT_TOO_SHORT_MSG + req.content());
    }

    if (req.content().length() > MAX_CONTENT_LENGTH) {
      throw new RuntimeException(ErrorMessage.CONTENT_TOO_LONG_MSG + req.content());
    }
  }

  /**
   * /boards/free/delete [POST]
   *
   * @param req
   */
  public void validateDeleteBoard(HttpServletRequest req) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_POST, req.getMethod())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + req.getMethod());
    }
  }

  /**
   * /processBoardDelete [POST]
   *
   * @param reqBoardDelete
   */
  public void validateDeleteExecutionBoard(ReqBoardDelete reqBoardDelete) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_POST, reqBoardDelete.method())) {
      throw new RuntimeException(ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + reqBoardDelete.method());
    }

    if (!checkPasswordMatch(reqBoardDelete.boardId(), reqBoardDelete.password())) {
      throw new RuntimeException(ErrorMessage.PASSWORD_NOT_MATCH_MSG);
    }
  }

  public void validateCreateCommentParams(ReqCommentCreate reqCommentCreate) {

    if (!checkHttpMethodMatch(Constants.HTTP_METHOD_POST, reqCommentCreate.method())) {
      throw new RuntimeException(
          ErrorMessage.HTTP_METHOD_NOT_MATCH_MSG + reqCommentCreate.method());
    }
  }
}
