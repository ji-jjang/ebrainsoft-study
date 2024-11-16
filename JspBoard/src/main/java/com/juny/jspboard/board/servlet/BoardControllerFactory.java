package com.juny.jspboard.board.servlet;

import com.juny.jspboard.board.controller.BoardController;
import com.juny.jspboard.board.controller.BoardCreateController;
import com.juny.jspboard.board.controller.BoardCreateExecutionController;
import com.juny.jspboard.board.controller.BoardDeleteController;
import com.juny.jspboard.board.controller.BoardDeleteExecutionController;
import com.juny.jspboard.board.controller.BoardDetailController;
import com.juny.jspboard.board.controller.BoardListController;
import com.juny.jspboard.board.controller.BoardModifyController;
import com.juny.jspboard.board.controller.BoardModifyExecutionController;
import com.juny.jspboard.board.controller.CommentCreateController;
import com.juny.jspboard.board.dao.AttachmentDAO;
import com.juny.jspboard.board.dao.AttachmentDAOImpl;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.BoardDAOImpl;
import com.juny.jspboard.board.dao.BoardImageDAO;
import com.juny.jspboard.board.dao.BoardImageDAOImpl;
import com.juny.jspboard.board.dao.CategoryDAO;
import com.juny.jspboard.board.dao.CategoryDAOImpl;
import com.juny.jspboard.board.dao.CommentDAO;
import com.juny.jspboard.board.dao.CommentDAOImpl;
import com.juny.jspboard.board.service.BoardService;
import com.juny.jspboard.validator.BoardValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BoardControllerFactory {

  private final BoardService boardService;
  private final BoardDAO boardDAO;
  private final BoardImageDAO boardImageDAO;
  private final AttachmentDAO attachmentDAO;
  private final CategoryDAO categoryDAO;
  private final CommentDAO commentDAO;
  private final BoardValidator validator;

  public BoardControllerFactory() {
    this.boardDAO = new BoardDAOImpl();
    this.boardImageDAO = new BoardImageDAOImpl();
    this.attachmentDAO = new AttachmentDAOImpl();
    this.categoryDAO = new CategoryDAOImpl();
    this.commentDAO = new CommentDAOImpl();
    this.validator = new BoardValidator();

    this.boardService =
        new BoardService(
            boardDAO, boardImageDAO, categoryDAO, attachmentDAO, commentDAO, validator);
  }

  public Map<String, BoardController> createExactMappings() {
    Map<String, BoardController> exactMappings = new HashMap<>();
    exactMappings.put("/boards/free/write/new", new BoardCreateController(boardService));
    exactMappings.put("/boards/free/list", new BoardListController(boardService));
    exactMappings.put("/boards/free/delete", new BoardDeleteController(boardService));
    exactMappings.put("/boards/free/write", new BoardCreateExecutionController(boardService));
    exactMappings.put("/boards/free/modify", new BoardModifyExecutionController(boardService));

    return exactMappings;
  }

  public Map<Pattern, BoardController> createRegexMappings() {
    Map<Pattern, BoardController> regexMappings = new HashMap<>();
    regexMappings.put(
        Pattern.compile("^/boards/free/view/[0-9]+"), new BoardDetailController(boardService));
    regexMappings.put(
        Pattern.compile("^/boards/free/modify/[0-9]+"), new BoardModifyController(boardService));
    regexMappings.put(
        Pattern.compile("^/boards/[0-9]+/comments$"), new CommentCreateController(boardService));
    regexMappings.put(
        Pattern.compile("^/boards/free/delete/[0-9]+"),
        new BoardDeleteExecutionController(boardService));

    return regexMappings;
  }

  public BoardService getBoardService() {
    return boardService;
  }

  public BoardValidator getValidator() {
    return validator;
  }
}
