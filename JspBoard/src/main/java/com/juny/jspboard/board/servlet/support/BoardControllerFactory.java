package com.juny.jspboard.board.servlet.support;

import com.juny.jspboard.board.controller.BoardController;
import com.juny.jspboard.board.controller.BoardCreateController;
import com.juny.jspboard.board.controller.BoardDeleteController;
import com.juny.jspboard.board.controller.BoardDeleteExecutionController;
import com.juny.jspboard.board.controller.BoardDetailController;
import com.juny.jspboard.board.controller.BoardListController;
import com.juny.jspboard.board.controller.BoardModifyController;
import com.juny.jspboard.board.controller.CommentCreateController;
import com.juny.jspboard.board.dao.BoardDAO;
import com.juny.jspboard.board.dao.CategoryDAO;
import com.juny.jspboard.validator.BoardValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BoardControllerFactory {
  private final CategoryDAO categoryDAO;
  private final BoardDAO boardDAO;
  private final BoardValidator validator;

  public BoardControllerFactory(
      CategoryDAO categoryDAO, BoardDAO boardDAO, BoardValidator validator) {
    this.categoryDAO = categoryDAO;
    this.boardDAO = boardDAO;
    this.validator = validator;
  }

  public Map<String, BoardController> createExactMappings() {
    Map<String, BoardController> exactMappings = new HashMap<>();
    exactMappings.put("/boards/free/write", new BoardCreateController(categoryDAO));
    exactMappings.put(
        "/boards/free/list", new BoardListController(boardDAO, categoryDAO, validator));
    exactMappings.put("/boards/free/delete", new BoardDeleteController(validator));

    return exactMappings;
  }

  public Map<Pattern, BoardController> createRegexMappings() {
    Map<Pattern, BoardController> regexMappings = new HashMap<>();
    regexMappings.put(
        Pattern.compile("^/boards/free/view/[0-9]+"),
        new BoardDetailController(boardDAO, validator));
    regexMappings.put(
        Pattern.compile("^/boards/free/modify/[0-9]+"),
        new BoardModifyController(boardDAO, validator));
    regexMappings.put(
        Pattern.compile("^/boards/[0-9]+/comments$"),
        new CommentCreateController(boardDAO, validator));
    regexMappings.put(
        Pattern.compile("^/boards/free/delete/[0-9]+"),
        new BoardDeleteExecutionController(boardDAO, validator));

    return regexMappings;
  }

  public BoardDAO createBoardDAO() {
    return boardDAO;
  }

  public BoardValidator createBoardValidator() {
    return validator;
  }
}
