package com.juny.jspboard.board.controller;

import com.juny.jspboard.board.dto.ReqBoardUpdate;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.utility.FileUtils;
import com.juny.jspboard.utility.dto.ResFileParsing;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class BoardModifyExecutionController implements BoardController {

  private final BoardService boardService;

  public BoardModifyExecutionController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   *
   *
   * <h1>게시판 수정 후처리: DB 반영 </h1>
   *
   * <br>
   * - 1. 게시판 이미지와 첨부 파일 처리 <br>
   * - 2. delete 선택된 목록은 DB와 파일시스템에서 제거 <br>
   * - 3. 이미 DB에 등록된 이미지와 첨부 파일은 로컬 파일시스템에 존재한다면 INSERT 쿼리에서 제외.
   *
   * @param req
   * @param res
   * @return View
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    ReqBoardUpdate reqBoardUpdate = extractReqBoardUpdate(req);

    boardService.modifyBoard(reqBoardUpdate);

    return Constants.REDIRECT_PREFIX + "/boards/free/view/" + reqBoardUpdate.boardId();
  }

  private ReqBoardUpdate extractReqBoardUpdate(HttpServletRequest req)
      throws ServletException, IOException {

    Long boardId = Long.parseLong(req.getParameter(Constants.BOARD_ID));
    String createdBy = req.getParameter(Constants.CREATED_BY);
    String title = req.getParameter(Constants.TITLE);
    String content = req.getParameter(Constants.CONTENT);
    String password = req.getParameter(Constants.PASSWORD);

    ResFileParsing files = FileUtils.parsingFiles(req.getParts());

    return new ReqBoardUpdate(
        boardId,
        createdBy,
        title,
        LocalDateTime.now().toString(),
        content,
        password,
        req.getMethod(),
        files.images(),
        files.attachments());
  }
}
