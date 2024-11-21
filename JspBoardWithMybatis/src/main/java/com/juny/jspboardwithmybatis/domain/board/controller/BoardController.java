package com.juny.jspboardwithmybatis.domain.board.controller;

import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardCreate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardUpdate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreUpdate;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardList;
import com.juny.jspboardwithmybatis.domain.board.service.BoardService;
import com.juny.jspboardwithmybatis.domain.utils.CategoryMapperUtils;
import com.juny.jspboardwithmybatis.domain.utils.FileUtils;
import com.juny.jspboardwithmybatis.domain.utils.dto.FileDetails;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

  private final BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
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
   * - 1. 파일 정보를 파싱 (storedName, storedPath, extension ...)<br>
   * - 2. 서비스 트랜잭션으로 게시판 생성(게시판, 이미지, 첨부파일)<br>
   * - 3. 트랜잭션 성공 시 파일 시스템에 저장
   *
   * @return View
   */
  @PostMapping("/boards")
  public String createBoard(@ModelAttribute ReqBoardCreate reqBoardCreate) {

    List<FileDetails> images = FileUtils.parseFileDetails(reqBoardCreate.getImages(), "images");
    List<FileDetails> attachments =
        FileUtils.parseFileDetails(reqBoardCreate.getAttachments(), "attachments");

    Long boardId = boardService.createBoard(reqBoardCreate, images, attachments);

    FileUtils.saveFile(reqBoardCreate.getImages(), images);
    FileUtils.saveFile(reqBoardCreate.getAttachments(), attachments);

    return "redirect:/boards/" + boardId;
  }

  /**
   *
   *
   * <h1>게시판 수정 폼 생성</h1>
   *
   * @return View
   */
  @GetMapping("/boards/{id}/update")
  public String createEditForm(@PathVariable Long id, Model model) {

    System.out.println("BoardController.createEditForm");
    ResBoardDetail board = boardService.getBoard(id);

    model.addAttribute("board", board);

    return "createUpdateForm";
  }

  /**
   *
   *
   * <h1>게시판 수정 </h1>
   *
   * <br>
   * - 요청 검증 위해 먼저 게시판 상세 조회 쿼리 실행<br>
   * - 전처리 과정: 추가할 파일 상세 정보와 제거할 파일 경로 DTO 추가<br>
   * - 트랜잭션 성공 시 파일 시스템에 파일 추가 및 삭제<br>
   * - 폼 메서드에선 [PUT, PATCH]를 사용할 수 없어 [POST] 사용<br>
   * - 게시판에 있지 않은 파일 ID 삭제 시도 시 에러 - 중복한 아이디 삭제 요청 시 에러
   *
   * @return View
   */
  @PostMapping("/boards/{id}")
  public String updateBoard(
      @PathVariable Long id, @ModelAttribute ReqBoardPreUpdate reqBoardPreUpdate) {

    ResBoardDetail board = boardService.getBoard(id);

    ReqBoardUpdate reqBoardUpdate = boardService.preProcessUpdate(board, reqBoardPreUpdate);

    boardService.updateBoard(id, reqBoardUpdate);

    FileUtils.saveFile(reqBoardUpdate.getImages(), reqBoardUpdate.getImageDetails());
    FileUtils.saveFile(reqBoardUpdate.getAttachments(), reqBoardUpdate.getAttachmentDetails());
    FileUtils.deleteFile(reqBoardUpdate.getDeleteFilePaths());

    return "redirect:/boards/" + id;
  }
}
