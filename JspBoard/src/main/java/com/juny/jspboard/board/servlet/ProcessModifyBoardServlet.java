package com.juny.jspboard.board.servlet;

import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardDAOImpl;
import com.juny.jspboard.board.dto.ReqBoardUpdate;
import com.juny.jspboard.board.servlet.support.BoardControllerFactory;
import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.constant.Env;
import com.juny.jspboard.utility.FileUtils;
import com.juny.jspboard.utility.dto.ResFileParsing;
import com.juny.jspboard.validator.BoardValidator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebServlet("/processModifyBoard")
@MultipartConfig
public class ProcessModifyBoardServlet extends HttpServlet {

  private BoardDAO boardDAO;
  private BoardValidator validator;

  @Override
  public void init() {
    BoardControllerFactory factory =
      (BoardControllerFactory) getServletContext().getAttribute("boardControllerFactory");

    this.boardDAO = factory.createBoardDAO();
    this.validator = factory.createBoardValidator();
    new File(Env.ATTACHMENT_PATH).mkdirs();
    new File(Env.IMAGE_PATH).mkdirs();
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  /**
   * 1. 게시판 이미지와 첨부 파일 처리 2. delete 선택된 목록은 DB와 파일시스템에서 제거 3. 이미 DB에 등록된 이미지와 첨부 파일은 로컬 파일시스템에 존재한다면
   * INSERT 쿼리에서 제외.
   *
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    ReqBoardUpdate reqBoardUpdate = extractReqBoardUpdate(req);

    validator.validateProcessModifyBoard(reqBoardUpdate);

    boardDAO.updateBoard(reqBoardUpdate);

    res.sendRedirect("/boards/free/view/" + reqBoardUpdate.boardId());
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
