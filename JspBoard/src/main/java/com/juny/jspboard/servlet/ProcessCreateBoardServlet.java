package com.juny.jspboard.servlet;

import com.juny.jspboard.dao.BoardDAO;
import com.juny.jspboard.dao.BoardDAOImpl;
import com.juny.jspboard.entity.Attachment;
import com.juny.jspboard.entity.Board;
import com.juny.jspboard.entity.BoardImage;
import com.juny.jspboard.validator.BoardValidator;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@WebServlet("/boards/processCreateBoard")
@MultipartConfig
public class ProcessCreateBoardServlet extends HttpServlet implements BoardControllerServlet {

  private final BoardDAO boardDAO = new BoardDAOImpl();

  private final BoardValidator validator = new BoardValidator();

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

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    String category = req.getParameter("category");
    String createdBy = req.getParameter("createdBy");
    String password = req.getParameter("password");
    String passwordConfirm = req.getParameter("passwordConfirm");
    String title = req.getParameter("title");
    String content = req.getParameter("content");

    validator.validateCreateBoardParams(
      category, createdBy, password, passwordConfirm, title, content);

    File uploadDir = new File(uploadPath);
    if (!uploadDir.exists()) uploadDir.mkdir();
    File imageDir = new File(imagePath);
    if (!imageDir.exists()) uploadDir.mkdir();

    List<Attachment> attachments = new ArrayList<>();
    List<BoardImage> images = new ArrayList<>();

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
          part.write(filePath);
          attachments.add(new Attachment(logicalName, "", uniqueFileName, uploadPath, extension, part.getSize()));

        } else if (part.getName().equals("images")) {

          filePath = imagePath + File.separator + uniqueFileName + extension;
          part.write(filePath);

          images.add(new BoardImage(uniqueFileName, imagePath, extension));
        }
      }
    }

    Long categoryId = boardDAO.getCategoryIdByName(category);
    Board board = new Board(title, content, password, 0, LocalDateTime.now(), createdBy, null, categoryId);

    Long boardId = boardDAO.createBoard(category, board, images, attachments);
    String redirectUrl = "/boards/free/view/" + boardId;
    res.sendRedirect(redirectUrl);
  }
}
