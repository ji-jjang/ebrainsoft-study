package com.juny.jspboardwithmybatis.domain.board.controller;

import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

  private final BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  /**
   * <h1>게시판 상세 페이지 조회 </h1
   * <br>- 조회수 증가와 상세 페이지 조회를 같은 트랜잭션으로 묶을 필요 없음
   * <br>- 오히려 비동기 처리할 것
   * @param id
   * @param model
   * @return View
   *
   */
  @GetMapping("/boards/{id}")
  public String getBoard(@PathVariable Long id, Model model) {

    boardService.increaseViewCount(id);
    ResBoardDetail board = boardService.getBoard(id);

    model.addAttribute("board", board);

    return "board";
  }
}
