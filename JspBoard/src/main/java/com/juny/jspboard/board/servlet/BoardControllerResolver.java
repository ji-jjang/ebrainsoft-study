package com.juny.jspboard.board.servlet;

import com.juny.jspboard.board.controller.BoardController;
import java.util.Map;
import java.util.regex.Pattern;

public class BoardControllerResolver {

  private final Map<String, BoardController> exactMappings;
  private final Map<Pattern, BoardController> regexMappings;

  public BoardControllerResolver(BoardControllerFactory factory) {
    this.exactMappings = factory.createExactMappings();
    this.regexMappings = factory.createRegexMappings();
  }

  public BoardController resolveController(String url) {
    if (exactMappings.containsKey(url)) {
      return exactMappings.get(url);
    }
    for (var entry : regexMappings.entrySet()) {
      if (entry.getKey().matcher(url).matches()) {
        return entry.getValue();
      }
    }
    return null;
  }
}
