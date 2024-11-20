package com.juny.jspboardwithmybatis.domain.board.controller;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorBeanPostProcessor;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardCreate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardList;
import com.juny.jspboardwithmybatis.domain.board.service.BoardService;
import com.juny.jspboardwithmybatis.domain.utils.CategoryMapperUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

  private final BoardService boardService;
  private final DataSourceDecoratorBeanPostProcessor dataSourceDecoratorBeanPostProcessor;

  public BoardController(BoardService boardService,
    DataSourceDecoratorBeanPostProcessor dataSourceDecoratorBeanPostProcessor) {
    this.boardService = boardService;
    this.dataSourceDecoratorBeanPostProcessor = dataSourceDecoratorBeanPostProcessor;
  }

  /**
   *
   *
   * <h1>게시판 상세 페이지 조회 </h1
   * <br>
   *
   * - 조회수 증가와 상세 페이지 조회를 같은 트랜잭션으로 묶을 필요 없음 <br>
   * - 오히려 비동기 처리할 것
   *
   * @param id
   * @param model
   * @return View
   */
  @GetMapping("/boards/{id}")
  public String getBoard(@PathVariable Long id, Model model) {

    boardService.increaseViewCount(id);
    ResBoardDetail board = boardService.getBoard(id);

    model.addAttribute("board", board);

    return "board";
  }

  /**
   *
   *
   * <h1>게시판 목록 페이지 조회 </h1>
   *
   * <br>
   * - 검색 조건을 유지하며, 페이지 이동 할 수 있어야 함
   *
   * @param model SearchCondition(startDate, endDate, categoryName, keyword), page
   * @return View
   */
  @GetMapping("/boards")
  public String getBoards(@ModelAttribute ReqBoardList reqBoardList, Model model) {

    ResBoardList boardList = boardService.getBoardList(reqBoardList);

    model.addAttribute("boards", boardList.getBoards());
    model.addAttribute("searchCondition", boardList.getSearchCondition());
    model.addAttribute("pageInfo", boardList.getPageInfo());
    model.addAttribute("categories", CategoryMapperUtils.getAllCategoryName());

    return "boards";
  }

  /**
   *
   *
   * <h1>게시판 생성 폼</h1>
   *
   * @param model
   * @return View
   */
  @GetMapping("/boards/new")
  public String createBoardForm(Model model) {

    model.addAttribute("categories", CategoryMapperUtils.getAllCategoryName());

    return "createBoardForm";
  }

  /**
   *
   *
   * <h1>게시판 생성 </h1>
   *
   * <br>
   * - PRG
   *
   * @return View
   */
  @PostMapping("/boards")
  public String createBoard(
    @ModelAttribute ReqBoardCreate reqBoardCreate) {

    System.out.println("reqBoardCreate.toString() = " + reqBoardCreate.toString());
    return "redirect:/boards + boardId";
  }
}
