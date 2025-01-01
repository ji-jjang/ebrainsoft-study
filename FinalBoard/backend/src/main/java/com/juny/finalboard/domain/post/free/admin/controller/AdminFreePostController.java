package com.juny.finalboard.domain.post.free.admin.controller;

import com.juny.finalboard.domain.post.common.LocalFileService;
import com.juny.finalboard.domain.post.free.common.dto.FileDownloadVo;
import com.juny.finalboard.domain.post.free.common.dto.FreeSearchCondition;
import com.juny.finalboard.domain.post.free.common.dto.FreeUpdateVO;
import com.juny.finalboard.domain.post.free.common.dto.ReqCreateFreeComment;
import com.juny.finalboard.domain.post.free.common.dto.ReqCreateFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ReqGetFreePostList;
import com.juny.finalboard.domain.post.free.common.dto.ReqUpdateFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ResFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ResFreePostList;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import com.juny.finalboard.domain.post.free.common.entity.FreePostCategory;
import com.juny.finalboard.domain.post.free.common.mapper.FreePostMapper;
import com.juny.finalboard.domain.post.free.common.service.FreeAttachmentService;
import com.juny.finalboard.domain.post.free.common.service.FreePostCategoryService;
import com.juny.finalboard.domain.post.free.common.service.FreePostService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
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
public class AdminFreePostController {

  private final FreePostService freePostService;

  private final FreePostCategoryService freePostCategoryService;

  private final FreeAttachmentService freeAttachmentService;

  private final LocalFileService localFileService;

  /**
   *
   *
   * <h1>관리자 자유 게시판 조회 </h1>
   *
   * @param model 카테고리, 게시글 목록, 검색조건, 페이지 정보 반환
   * @param req 목록 조회 DTO
   * @return 목록
   */
  @GetMapping("/admin/free/board")
  public String getFreeBoard(Model model, @ModelAttribute ReqGetFreePostList req) {

    FreeSearchCondition searchCondition = freePostService.createSearchCondition(req);

    long totalPostCount = freePostService.getTotalPostCount(searchCondition);

    List<FreePost> freePostList = freePostService.getFreePostListBySearchCondition(searchCondition);

    ResFreePostList resFreePostList =
        FreePostMapper.toResFreePostList(freePostList, searchCondition, totalPostCount);

    List<FreePostCategory> categoryList = freePostCategoryService.getAllCategories();

    model.addAttribute("categoryList", categoryList);
    model.addAttribute("postList", resFreePostList.postList());
    model.addAttribute("searchCondition", resFreePostList.searchCondition());
    model.addAttribute("pageInfo", resFreePostList.pageInfo());

    return "admin/free/board";
  }

  /**
   *
   *
   * <h1>자유 게시글 상세 조회 </h1>
   *
   * @param model categories, post
   * @param postId 게시글 아이디
   * @return 상세 게시글
   */
  @GetMapping("/admin/free/post/{postId}")
  public String getFreePost(Model model, @PathVariable Long postId) {

    freePostService.increaseViewCount(postId);

    FreePost freePost = freePostService.getFreePostDetail(postId);

    List<FreePostCategory> categoryList = freePostCategoryService.getAllCategories();

    ResFreePost resFreePost = FreePostMapper.toResFreePost(freePost);

    model.addAttribute("categories", categoryList);

    model.addAttribute("post", resFreePost);

    return "admin/free/post";
  }

  /**
   *
   *
   * <h1>게시글 생성 폼 </h1>
   *
   * @param model 카테고리
   * @return 게시글 생성 폼
   */
  @GetMapping("/admin/free/create-form")
  public String getCreateForm(Model model) {

    List<FreePostCategory> categories = freePostCategoryService.getAllCategories();

    model.addAttribute("categories", categories);

    return "admin/free/create-form";
  }

  /**
   *
   *
   * <h1>게시글 생성 </h1>
   *
   * @param req 게시글 생성 폼 DTO
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색어
   * @param originCategoryId 기존 카테고리 아이디
   * @param pageSize 페이지 사이즈
   * @param sort 정렬
   * @param userDetails 인증된 유저 정보
   * @return 게시글
   */
  @PostMapping("/admin/free/create")
  public String createAnnouncementPost(
      @ModelAttribute @Valid ReqCreateFreePost req,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    FreePost freePost = freePostService.createFreePost(req, userDetails.getId());

    localFileService.saveFile(req.attachments(), freePost.getFreeAttachmentList());

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
   * <h1>게시글 수정 폼 </h1>
   *
   * @param postId 게시글 아이디
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색어
   * @param originCategoryId 기존 카테고리 아이디
   * @param pageSize 페이지 사이즈
   * @param sort 정렬
   * @param model 게시글, 카테고리, 검색조건들
   * @return 게시글 수정 폼
   */
  @GetMapping("/admin/free/post/{postId}/update-form")
  public String getUpdateForm(
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      Model model) {

    FreePost freePost = freePostService.getFreePostDetail(postId);

    ResFreePost resFreePost = FreePostMapper.toResFreePost(freePost);

    List<FreePostCategory> categories = freePostCategoryService.getAllCategories();

    model.addAttribute("post", resFreePost);
    model.addAttribute("categories", categories);

    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("originCategoryId", originCategoryId);
    model.addAttribute("keyword", keyword);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("sort", sort);

    return "admin/free/update-form";
  }

  /**
   *
   *
   * <h1>첨부파일 조회, 수정 시에 클릭하면 다운로드 </h1>
   *
   * @param attachmentId 첨부파일 아이디
   * @param res Void
   */
  @GetMapping("/admin/free/attachment/{attachmentId}/download")
  public void downloadAttachment(@PathVariable Long attachmentId, HttpServletResponse res) {

    FileDownloadVo fileDownloadVO = freeAttachmentService.getAttachmentStoredPath(attachmentId);

    localFileService.downloadFile(fileDownloadVO, res);
  }

  /**
   *
   *
   * <h1>자유 게시글 수정 </h1>
   *
   * @param reqUpdateFreePost 수정 폼
   * @param postId 게시글 아이디
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색어
   * @param originCategoryId 기존 카테고리 아이디
   * @param pageSize 페이지 사이즈
   * @param sort 정렬
   * @return 수정한 게시글
   */
  @PostMapping("/admin/free/post/{postId}/update")
  public String postUpdate(
      @ModelAttribute ReqUpdateFreePost reqUpdateFreePost,
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    FreePost freePost = freePostService.getFreePostDetail(postId);

    FreeUpdateVO freeUpdateVO =
        freePostService.updatePost(reqUpdateFreePost, freePost, userDetails.getId());

    localFileService.saveFile(
        freeUpdateVO.addMultipartFileList(), freeUpdateVO.addAttachmentList());
    localFileService.deleteFile(freeUpdateVO.deleteAttachmentList());

    return String.format(
        "redirect:/admin/free/post/%d?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        postId,
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
   * <h1>게시글 삭제 </h1>
   *
   * @param postId 게시글 아이디
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색어
   * @param categoryId 카테고리 아이디
   * @param pageSize 페이지 사이즈
   * @param sort 정렬
   * @param userDetails 인증된 유저 정보
   * @return Void
   */
  @PostMapping("/admin/free/post/{postId}/delete")
  public String deleteFreePost(
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    FreePost freePost = freePostService.getFreePostDetail(postId);

    freePostService.deletePostById(postId, freePost, userDetails.getId());

    return String.format(
        "redirect:/admin/free/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        startDate,
        endDate,
        categoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }

  /**
   *
   *
   * <h1>게시글에 댓글 생성 </h1>
   *
   * @param req 댓글 생성 폼 DTO
   * @param postId 게시글 아이디
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색어
   * @param pageSize 페이지 사이즈
   * @param sort 정렬
   * @return 댓글 생성 게시글
   */
  @PostMapping("/admin/free/post/{postId}/comment")
  public String createComment(
      ReqCreateFreeComment req,
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    freePostService.createComment(req, postId, userDetails.getId());

    return String.format(
        "redirect:/admin/free/post/%d?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        postId,
        startDate,
        endDate,
        categoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }
}
