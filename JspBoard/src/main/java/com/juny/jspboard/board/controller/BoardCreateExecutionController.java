package com.juny.jspboard.board.controller;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dto.ReqBoardCreate;
import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.Board;
import com.juny.jspboard.board.entity.BoardImage;
import com.juny.jspboard.utility.FileUtils;
import com.juny.jspboard.utility.dto.ResFileParsing;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/** 사용자가 입력한 게시판 생성 정보를 가지고, 이미지나 첨부 파일을 로컬 파일 시스템과 DB에 저장하고, 게시판을 DB에 저장함. */
public class BoardCreateExecutionController implements BoardController {

  private final BoardDAO boardDAO;
  private final BoardValidator validator;

  public BoardCreateExecutionController(BoardDAO boardDAO, BoardValidator validator) {
    this.boardDAO = boardDAO;
    this.validator = validator;
  }

  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    ReqBoardCreate reqBoardCreate = extractReqBoardCreate(req);
    validator.validateCreateBoardParams(reqBoardCreate);

    ResFileParsing files = FileUtils.parsingFiles(req);
    Long boardId = createAndSaveBoard(reqBoardCreate, files.images(), files.attachments());

    return Constants.REDIRECT_PREFIX + "/boards/free/view/" + boardId;
  }

  private ReqBoardCreate extractReqBoardCreate(HttpServletRequest req) {
    String category = req.getParameter(Constants.CATEGORY);
    String createdBy = req.getParameter(Constants.CREATED_BY);
    String password = req.getParameter(Constants.PASSWORD);
    String passwordConfirm = req.getParameter(Constants.PASSWORD_CONFIRM);
    String title = req.getParameter(Constants.TITLE);
    String content = req.getParameter(Constants.CONTENT);
    String method = req.getMethod();

    return new ReqBoardCreate(
        category, createdBy, password, passwordConfirm, title, content, method);
  }

  private Long createAndSaveBoard(
      ReqBoardCreate reqBoardCreate, List<BoardImage> images, List<Attachment> attachments) {
    Long categoryId = boardDAO.getCategoryIdByName(reqBoardCreate.category());

    Board board =
        new Board(
            reqBoardCreate.title(),
            reqBoardCreate.title(),
            reqBoardCreate.password(),
            0,
            LocalDateTime.now(),
            reqBoardCreate.createdBy(),
            null,
            categoryId);

    return boardDAO.createBoard(reqBoardCreate.category(), board, images, attachments);
  }
}
