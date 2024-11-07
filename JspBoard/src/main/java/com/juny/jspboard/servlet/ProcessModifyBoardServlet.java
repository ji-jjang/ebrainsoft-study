package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import com.juny.jspboard.dto.ReqBoardUpdate;
import com.juny.jspboard.entity.Attachment;
import com.juny.jspboard.entity.BoardImage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@WebServlet("/boards/processModifyBoard")
@MultipartConfig
public class ProcessModifyBoardServlet extends HttpServlet implements BoardControllerServlet {

  private final BoardDAO boardDAO = new BoardDAOImpl();
  private String uploadPath;
  private String imagePath;

  @Override
  public void init() throws ServletException {
    ServletContext context = getServletContext();
    this.uploadPath = context.getRealPath("/attachments/");
    this.imagePath = context.getRealPath("/images/");

    new File(uploadPath).mkdirs();
    new File(imagePath).mkdirs();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    execute(req, res);
  }

  /**
   * 게시판 이미지와 첨부 파일 처리 -> delete 선택된 목록은 DB와 파일시스템에서 제거,
   *                         -> 이미 DB에 등록된 이미지와 첨부 파일은 로컬 파일시스템에 존재한다면 INSERT 쿼리에서 제외.
   * @param req
   * @param res
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    ReqBoardUpdate reqBoardUpdate = null;
    List<String> deleteAttachments = null;
    List<String> deleteImages = null;
    List<Attachment> attachments = new ArrayList<>();
    List<BoardImage> images = new ArrayList<>();

    Long boardId = Long.parseLong(req.getParameter("boardId"));
    String updatedAt = LocalDateTime.now().toString();
    String createdBy = req.getParameter("createdBy");
    String password = req.getParameter("updatedBy");
    String title = req.getParameter("title");
    String content = req.getParameter("content");

    // TODO server가 저장한 password와 일치여부 탐색(alert와 함께 상세페이지로 이동)

    if (!Objects.isNull(req.getParameterValues("deleteAttachments"))) {
      deleteAttachments = Arrays.stream(req.getParameterValues("deleteAttachments")).toList();
    }
    if (!Objects.isNull(req.getParameterValues("deleteImages"))) {
      deleteImages = Arrays.stream(req.getParameterValues("deleteImages")).toList();
    }

    for (var part : req.getParts()) {
      if (part.getSize() > 0) {
        String logicalName = part.getSubmittedFileName();
        String extension = "";

        if (!Objects.isNull(logicalName) && logicalName.contains(".")) {
          extension = logicalName.substring(logicalName.lastIndexOf("."));
          logicalName = logicalName.substring(0, logicalName.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID().toString();
        String filePath = "";
        if (part.getName().equals("files")) {

          filePath = uploadPath + File.separator + uniqueFileName + extension;
          File file = new File(filePath);
          if (!file.exists()) {
            part.write(filePath);
            attachments.add(new Attachment(logicalName, "", uniqueFileName, uploadPath, extension, part.getSize()));
          }
        } else if (part.getName().equals("images")) {

          filePath = imagePath + File.separator + uniqueFileName + extension;
          File file = new File(filePath);
          if (!file.exists()) {
            part.write(filePath);
            images.add(new BoardImage(uniqueFileName, imagePath, extension));
          }
        }
      }
    }

    reqBoardUpdate = new ReqBoardUpdate(
      boardId,
      createdBy,
      title,
      updatedAt,
      content,
      deleteAttachments,
      deleteImages,
      images,
      attachments
    );

    boardDAO.updateBoard(reqBoardUpdate);
    String redirectUrl = "/boards/free/view/" + boardId;
    res.sendRedirect(redirectUrl);
  }
}
