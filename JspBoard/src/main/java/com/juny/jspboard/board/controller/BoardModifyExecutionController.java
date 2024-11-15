package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dto.ReqBoardUpdate;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.utility.FileUtils;
import com.juny.jspboard.utility.dto.ResFileParsing;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BoardModifyExecutionController implements BoardController {

  private final BoardDAO boardDAO;
  private final BoardValidator validator;

  public BoardModifyExecutionController(BoardDAO boardDAO, BoardValidator validator) {
    this.boardDAO = boardDAO;
    this.validator = validator;
  }

  /**
   * 1. 게시판 이미지와 첨부 파일 처리 2. delete 선택된 목록은 DB와 파일시스템에서 제거 3. 이미 DB에 등록된 이미지와 첨부 파일은 로컬 파일시스템에 존재한다면
   * INSERT 쿼리에서 제외.
   *
   * @param req
   * @param res
   * @return
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    ReqBoardUpdate reqBoardUpdate = extractReqBoardUpdate(req);

    validator.validateProcessModifyBoard(reqBoardUpdate);

    boardDAO.updateBoard(reqBoardUpdate);

    return Constants.REDIRECT_PREFIX + "/boards/free/view/" + reqBoardUpdate.boardId();
  }

  private ReqBoardUpdate extractReqBoardUpdate(HttpServletRequest req)
      throws ServletException, IOException {
    List<String> deleteAttachments = null;
    List<String> deleteImages = null;

    Long boardId = Long.parseLong(req.getParameter(Constants.BOARD_ID));
    String createdBy = req.getParameter(Constants.CREATED_BY);
    String title = req.getParameter(Constants.TITLE);
    String content = req.getParameter(Constants.CONTENT);
    String password = req.getParameter(Constants.PASSWORD);

    if (!Objects.isNull(req.getParameterValues(Constants.DELETE_ATTACHMENTS))) {
      deleteAttachments =
          Arrays.stream(req.getParameterValues(Constants.DELETE_ATTACHMENTS)).toList();
    }
    if (!Objects.isNull(req.getParameterValues(Constants.DELETE_IMAGES))) {
      deleteImages = Arrays.stream(req.getParameterValues(Constants.DELETE_IMAGES)).toList();
    }

    ResFileParsing files = FileUtils.parsingFiles(req);

    return new ReqBoardUpdate(
        boardId,
        createdBy,
        title,
        LocalDateTime.now().toString(),
        content,
        password,
        req.getMethod(),
        deleteAttachments,
        deleteImages,
        files.images(),
        files.attachments());
  }
}
