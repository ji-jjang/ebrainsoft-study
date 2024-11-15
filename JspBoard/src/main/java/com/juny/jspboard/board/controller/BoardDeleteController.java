package com.juny.jspboard.board.controller;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.utility.FileUtils;
import com.juny.jspboard.utility.dto.ResDeleteFileParsing;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardDeleteController implements BoardController {

  private final BoardValidator validator;

  public BoardDeleteController(BoardValidator validator) {
    this.validator = validator;
  }

  /**
   * 게시판을 삭제하기 전에 게시판에 달린 첨부파일과 이미지 파일을 먼저 삭제 첨부 파일과 이미지 파일 정보를 DB에서 조회하는 게 아닌 JSP 상세 페이지에서 정보를 받아오기
   * 때문에 파싱 작업 필요 BoardDeleteController -> 파일 정보 파싱 -> JSP 화면에서 비밀번호 입력 -> ProcessDelete에서 첨부파일과 이미지
   * 파일 삭제
   *
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    validator.validateDeleteBoard(req);

    ResDeleteFileParsing resDeleteFiles = FileUtils.parseDeleteFilePaths(req);

    req.setAttribute(Constants.DELETE_IMAGES, resDeleteFiles.deleteImages());
    req.setAttribute(Constants.DELETE_ATTACHMENTS, resDeleteFiles.deleteAttachments());
    req.setAttribute(Constants.DELETE_COMMENTS, resDeleteFiles.deleteComments());
    return "/deleteBoardPasswordCheck.jsp";
  }
}
