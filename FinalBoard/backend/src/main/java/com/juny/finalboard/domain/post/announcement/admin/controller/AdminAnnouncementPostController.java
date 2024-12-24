package com.juny.finalboard.domain.post.announcement.admin.controller;

import com.juny.finalboard.domain.post.announcement.admin.service.AdminAnnouncementPostService;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqGetPostList;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostCreate;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostUpdate;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementCategoryList;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementPostList;
import com.juny.finalboard.domain.post.announcement.common.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementCategory;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.mapper.AnnouncementCategoryMapper;
import com.juny.finalboard.domain.post.announcement.common.mapper.AnnouncementPostMapper;
import com.juny.finalboard.domain.post.announcement.common.service.AnnouncementCategoryService;
import com.juny.finalboard.domain.post.announcement.common.service.AnnouncementPostService;
import com.juny.finalboard.global.constant.Constants;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminAnnouncementPostController {

  private final AnnouncementPostService announcementPostService;

  private final AnnouncementCategoryService announcementCategoryService;

  private final AdminAnnouncementPostService adminAnnouncementPostService;

  /**
   *
   *
   * <h1>관리자 공지사항 목록 </h1>
   *
   * @param model 카테고리, 고정된 게시글들, 비고정 게시글들, 검색 조건, 페이지 정보
   * @param reqGetPostList 검색 파라미터
   * @return 게시글 목록
   */
  @GetMapping("/admin/announcement/board")
  public String getAnnouncementBoard(Model model, @ModelAttribute ReqGetPostList reqGetPostList) {

    List<AnnouncementPost> pinnedPostList =
        announcementPostService.getPinnedPostList(Constants.MAX_PINNED_POST);

    SearchCondition searchCondition = announcementPostService.createSearchCondition(reqGetPostList);

    long totalBoardCount = announcementPostService.getTotalBoardCount(searchCondition);

    List<AnnouncementPost> postList =
        announcementPostService.getPostListBySearchCondition(searchCondition);

    ResAnnouncementPostList resAnnouncementPostList =
        AnnouncementPostMapper.toResAnnouncementPostList(
            pinnedPostList, postList, searchCondition, totalBoardCount);

    List<AnnouncementCategory> categories = announcementCategoryService.getAllCategories();

    ResAnnouncementCategoryList resCategories =
        AnnouncementCategoryMapper.toResAnnouncementCategoryList(categories);

    model.addAttribute("categories", resCategories.resCategoryList());
    model.addAttribute("pinnedPostList", resAnnouncementPostList.resPinnedPostList());
    model.addAttribute("unPinnedPostList", resAnnouncementPostList.resUnPinnedPostList());
    model.addAttribute("searchCondition", resAnnouncementPostList.searchCondition());
    model.addAttribute("pageInfo", resAnnouncementPostList.pageInfo());

    return "admin/announcement/board";
  }

  /**
   *
   *
   * <h1>관리자 공지사항 게시글 상세 조회</h1>
   *
   * @param model 카테고리, 게시글
   * @param postId 게시글 아이디
   * @return 상세 게시글
   */
  @GetMapping("/admin/announcement/post/{postId}")
  public String getAnnouncePost(Model model, @PathVariable Long postId) {

    AnnouncementPost post = announcementPostService.getPostById(postId);

    List<AnnouncementCategory> categories = announcementCategoryService.getAllCategories();

    ResAnnouncementCategoryList resAnnouncementCategoryList =
        AnnouncementCategoryMapper.toResAnnouncementCategoryList(categories);

    ResAnnouncementPost resAnnouncementPost = AnnouncementPostMapper.toResAnnouncementPost(post);

    model.addAttribute("categories", resAnnouncementCategoryList.resCategoryList());

    model.addAttribute("post", resAnnouncementPost);

    return "admin/announcement/post";
  }

  /**
   *
   *
   * <h1>관리자 게시글 생성 폼 </h1>
   *
   * @param model 카테고리
   * @return 생성 폼
   */
  @GetMapping("/admin/announcement/create-form")
  public String getCreateForm(Model model) {

    List<AnnouncementCategory> categories = announcementCategoryService.getAllCategories();

    model.addAttribute("categories", categories);

    return "admin/announcement/create-form";
  }

  /**
   *
   *
   * <h1>관리자 게시글 생성 </h1>
   *
   * @param req 생성 폼 데이터
   * @param startDate 시작일(검색 조건)
   * @param endDate 종료일(검색 조건)
   * @param keyword 키워드(검색 조건)
   * @param originCategoryId 카테고리 아이디(검색 조건)
   * @param userDetails 게시글 수정자 작성하기 위함
   * @return 게시글 목록(생성하기 이전 검색조건 유지)
   */
  @PostMapping("/admin/announcement/create")
  public String createAnnouncementPost(
      @ModelAttribute @Valid ReqPostCreate req,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    adminAnnouncementPostService.createPost(req, userDetails.getId());

    return String.format(
      "redirect:/admin/announcement/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
      startDate,
      endDate,
      originCategoryId,
      keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
      pageSize,
      sort);
  }

  /**
   *
   *
   * <h1>관리자 게시글 수정 </h1>
   *
   * @param postId 게시글 아이디
   * @param req 수정 폼
   * @param startDate 시작일 (검색 조건)
   * @param endDate 종료일 (검색 조건)
   * @param keyword 키워드 (검색 조건)
   * @param originCategoryId 카테고리 아이디(검색 조건)
   * @param userDetails 수정한 사람 추적하기 위함
   * @return 게시글 목록 (수정하기 전 검색 조건 유지)
   */
  @PostMapping("/admin/announcement/post/{postId}/update")
  public String updateAnnouncementBoard(
      @PathVariable Long postId,
      @ModelAttribute @Valid ReqPostUpdate req,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    adminAnnouncementPostService.updatePost(req, postId, userDetails.getId());

    return String.format(
        "redirect:/admin/announcement/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        startDate,
        endDate,
        originCategoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }

  @PostMapping("/admin/announcement/post/{postId}/delete")
  public String deleteAnnouncementPost(
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort) {

    adminAnnouncementPostService.deleteAnnouncementPostById(postId);

    return String.format(
        "redirect:/admin/announcement/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        startDate,
        endDate,
        categoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }
}
