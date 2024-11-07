package com.juny.jspboard.validator;

import com.juny.jspboard.constant.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;

public class BoardValidator {

  // TODO BoardList에 대한 요청 유효성 검사
  public void validateBoardList(HttpServletRequest req) {

  }

  public Long validateBoardDetail(HttpServletRequest req) {

    String suffix = req.getRequestURI().substring(req.getRequestURI().lastIndexOf("/") + 1);
    String seq = suffix.substring(0);

    try {
      Integer.parseInt(seq);
    } catch (NumberFormatException e) {
      throw new RuntimeException(ErrorMessage.INVALID_BOARD_SEQ_MSG + seq);
    }
    return Long.parseLong(seq);
  }

  // TODO 게시판 생성 인자 유효성 검사
  public void validateCreateBoardParams(String category, String createdBy, String password, String passwordConfirm, String title, String content) {

  }
}
