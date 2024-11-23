package com.juny.jspboardwithmybatis.domain.board.controller;

import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardCreate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreCreate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardDelete;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreList;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardUpdate;
import com.juny.jspboardwithmybatis.domain.board.dto.ReqBoardPreUpdate;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ResPageInfo;
import com.juny.jspboardwithmybatis.domain.board.dto.ResSearchCondition;
import com.juny.jspboardwithmybatis.domain.board.service.BoardService;
import com.juny.jspboardwithmybatis.domain.utils.CategoryMapperUtils;
import com.juny.jspboardwithmybatis.domain.utils.FileUtils;
import com.juny.jspboardwithmybatis.global.Constants;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
   * - 검색 조건을 유지하며, 페이지 이동 할 수 있어야 함<br>
   * - 검색 조건이 세션에 있다면, 갱신
   *
   * @param model SearchCondition(startDate, endDate, categoryName, keyword), page
   * @return View
   */
  @GetMapping("/boards")
  public String getBoards(
      @ModelAttribute ReqBoardPreList reqBoardPreList, Model model, HttpSession session) {

    ReqBoardList reqBoardList = setSession(reqBoardPreList, session);

    ResBoardList boardList = boardService.getBoardList(reqBoardList);

    session.setAttribute("searchCondition", boardList.getSearchCondition());
    session.setAttribute("pageInfo", boardList.getPageInfo());
    session.setMaxInactiveInterval(Constants.SESSION_ACTIVE_TIME_SECOND);

    model.addAttribute("boards", boardList.getBoards());
    model.addAttribute("searchCondition", boardList.getSearchCondition());
    model.addAttribute("pageInfo", boardList.getPageInfo());
    model.addAttribute("categories", CategoryMapperUtils.getAllCategoryName());

    return "boards";
  }

  private ReqBoardList setSession(ReqBoardPreList reqBoardPreList, HttpSession session) {

    LocalDate startDate = reqBoardPreList.getStartDate();
    LocalDate endDate = reqBoardPreList.getEndDate();
    String categoryName = reqBoardPreList.getCategoryName();
    String keyword = reqBoardPreList.getKeyword();
    int page = reqBoardPreList.getPage() == null ? 1 : reqBoardPreList.getPage();

    ResSearchCondition searchConditionSession =
        (ResSearchCondition) session.getAttribute("searchCondition");

    ResPageInfo pageInfoSession = (ResPageInfo) session.getAttribute("pageInfo");

    if (startDate == null
        && searchConditionSession != null
        && searchConditionSession.getStartDate() != null) {
      startDate =
          LocalDate.parse(
              searchConditionSession.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    if (endDate == null
        && searchConditionSession != null
        && searchConditionSession.getEndDate() != null) {
      endDate =
          LocalDate.parse(
              searchConditionSession.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    if (categoryName == null
        && searchConditionSession != null
        && searchConditionSession.getCategoryName() != null) {
      categoryName = searchConditionSession.getCategoryName();
    }

    if (keyword == null
        && searchConditionSession != null
        && searchConditionSession.getKeyword() != null) {
      keyword = searchConditionSession.getKeyword();
    }

    if (page == 0 && pageInfoSession != null) {
      page = pageInfoSession.getPage();
    }

    if (startDate == null) {
      startDate = LocalDate.now().minusYears(1);
    }

    if (endDate == null) {
      endDate = LocalDate.now();
    }

    if (page == 0) {
      page = Constants.DEFAULT_PAGE_NUMBER;
    }

    return new ReqBoardList(startDate, endDate, categoryName, keyword, page);
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
   * - 1. 생성 전처리(저장할 파일 정보 파싱)<br>
   * - 2. 서비스 트랜잭션으로 게시판 생성(게시판, 이미지, 첨부파일)<br>
   * - 3. 트랜잭션 성공 시 파일 시스템에 저장
   *
   * @return View
   */
  @PostMapping("/boards")
  public String createBoard(@ModelAttribute ReqBoardPreCreate reqBoardPreCreate) {

    ReqBoardCreate reqBoardCreate = boardService.preProcessCreate(reqBoardPreCreate);

    Long boardId = boardService.createBoard(reqBoardCreate);

    FileUtils.saveFile(reqBoardCreate.getImages(), reqBoardCreate.getImageDetails());
    FileUtils.saveFile(reqBoardCreate.getAttachments(), reqBoardCreate.getAttachmentDetails());

    return Constants.REDIRECT_PREFIX + "/boards/" + boardId;
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
   *
   * @return View
   */
  @PostMapping("/boards/{id}")
  public String updateBoard(
      @PathVariable Long id, @ModelAttribute ReqBoardPreUpdate reqBoardPreUpdate) {

    ResBoardDetail board = boardService.getBoard(id);

    ReqBoardUpdate reqBoardUpdate = boardService.preProcessUpdate(id, board, reqBoardPreUpdate);

    boardService.updateBoard(id, reqBoardUpdate);

    FileUtils.saveFile(reqBoardUpdate.getImages(), reqBoardUpdate.getImageDetails());
    FileUtils.saveFile(reqBoardUpdate.getAttachments(), reqBoardUpdate.getAttachmentDetails());
    FileUtils.deleteFile(reqBoardUpdate.getDeleteFilePaths());

    return Constants.REDIRECT_PREFIX + "/boards/" + id;
  }

  /**
   *
   *
   * <h1>게시판 삭제 폼 생성 </h1>
   *
   * <br>
   * - 게시판 비밀번호를 입력받아 게시판 삭제 로직 실행
   *
   * @param id
   * @param model
   * @return View
   */
  @GetMapping("/boards/{id}/deleteForm")
  public String deleteBoard(@PathVariable Long id, Model model) {

    model.addAttribute("id", id);

    return "createDeleteForm";
  }

  /**
   *
   *
   * <h1>게시판 삭제 </h1>
   *
   * <br>
   * - 삭제하기 전 삭제할 이미지, 첨부파일 경로 전처리<br>
   * - 트랜잭션 안에서 댓글 -> 첨부파일 -> 게시판 이미지 -> 게시판 순으로 삭제<br>
   * - 트랜잭션이 성공했다면 파일 삭제
   *
   * @param id
   * @param password
   * @return View
   */
  @PostMapping("/boards/{id}/delete")
  public String deleteBoard(@PathVariable Long id, @RequestParam String password) {

    ResBoardDetail board = boardService.getBoard(id);

    ReqBoardDelete reqBoardDelete = boardService.preProcessDelete(id, password, board);

    boardService.deleteBoard(reqBoardDelete);

    FileUtils.deleteFile(reqBoardDelete.getDeleteFilePaths());

    return Constants.REDIRECT_PREFIX + "/boards";
  }
}
