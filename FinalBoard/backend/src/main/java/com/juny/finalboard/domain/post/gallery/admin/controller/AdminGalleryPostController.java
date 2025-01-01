package com.juny.finalboard.domain.post.gallery.admin.controller;

import com.juny.finalboard.domain.post.common.LocalFileService;
import com.juny.finalboard.domain.post.free.common.dto.FileDownloadVo;
import com.juny.finalboard.domain.post.gallery.common.dto.GallerySearchCondition;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqCreateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqGetGalleryPostList;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqUpdateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ResGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ResGalleryPostList;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryCategory;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryUpdateVO;
import com.juny.finalboard.domain.post.gallery.common.mapper.GalleryPostMapper;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryCategoryService;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryImageService;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryService;
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
public class AdminGalleryPostController {

  private final GalleryService galleryService;

  private final GalleryCategoryService galleryCategoryService;

  private final GalleryImageService galleryImageService;

  private final LocalFileService localFileService;

  /**
   *
   *
   * <h1>게시판 생성 </h1>
   *
   * @param req 생성 폼 DTO
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색 키워드
   * @param originCategoryId 기존 카테고리 아이디
   * @param pageSize 페이지 크기
   * @param sort 정렬
   * @param userDetails 인증 정보
   * @return 게시판 목록 화면 이동
   */
  @PostMapping("/admin/gallery/create")
  public String createGalleryPost(
      @ModelAttribute @Valid ReqCreateGalleryPost req,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    GalleryPost galleryPost = galleryService.createGalleryPost(req, userDetails.getId());

    localFileService.saveFile(req.images(), galleryPost.getGalleryImages());

    return String.format(
        "redirect:/admin/gallery/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
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
   * <h1>게시판 목록 화면 </h1>
   *
   * @param model model
   * @param req 검색 조건
   * @return 게시판 목록 화면
   */
  @GetMapping("/admin/gallery/board")
  public String getGalleryBoard(Model model, @ModelAttribute ReqGetGalleryPostList req) {

    GallerySearchCondition searchCondition = galleryService.createSearchCondition(req);

    long totalGalleryPostCount = galleryService.getTotalPostCountBySearchCondition(searchCondition);

    List<GalleryPost> galleryPosts =
        galleryService.getGalleryPostListBySearchCondition(searchCondition);

    ResGalleryPostList resGalleryPostList =
        GalleryPostMapper.toResGalleryPostList(
            galleryPosts, searchCondition, totalGalleryPostCount);

    List<GalleryCategory> categoryList = galleryCategoryService.getAllCategories();

    model.addAttribute("categoryList", categoryList);
    model.addAttribute("postList", resGalleryPostList.resGalleryPostList());
    model.addAttribute("searchCondition", resGalleryPostList.resSearchCondition());
    model.addAttribute("pageInfo", resGalleryPostList.resPageInfo());

    return "admin/gallery/board";
  }

  /**
   *
   *
   * <h1>게시글 상세 조회 </h1>
   *
   * @param model model
   * @param postId 조회할 게시글 번호
   * @return 게시글 상세 화면
   */
  @GetMapping("/admin/gallery/post/{postId}")
  public String getGalleryPost(Model model, @PathVariable Long postId) {

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(postId);

    List<GalleryCategory> categories = galleryCategoryService.getAllCategories();

    ResGalleryPost resGalleryPost = GalleryPostMapper.toResGalleryPost(galleryPost);

    System.out.println("resGalleryPost = " + resGalleryPost);

    model.addAttribute("categories", categories);

    model.addAttribute("post", resGalleryPost);

    return "admin/gallery/post";
  }

  /**
   *
   *
   * <h1>게시글 생성 폼</h1>
   *
   * @param model model
   * @return 생성 화면
   */
  @GetMapping("/admin/gallery/create-form")
  public String getCreateForm(Model model) {

    List<GalleryCategory> categories = galleryCategoryService.getAllCategories();

    model.addAttribute("categories", categories);

    System.out.println("AdminGalleryPostController.getCreateForm");

    return "admin/gallery/create-form";
  }

  /**
   *
   *
   * <h1>게시글 수정 폼 </h1>
   *
   * @param postId 게시글 아이디
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색 키워드
   * @param originCategoryId 기존 카테고리 아이디
   * @param pageSize 페이지 크기
   * @param sort 정렬
   * @param model model
   * @return 수정 폼
   */
  @GetMapping("/admin/gallery/post/{postId}/update-form")
  public String getUpdateForm(
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      Model model) {

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(postId);

    ResGalleryPost resGalleryPost = GalleryPostMapper.toResGalleryPost(galleryPost);

    List<GalleryCategory> categories = galleryCategoryService.getAllCategories();

    model.addAttribute("post", resGalleryPost);
    model.addAttribute("categories", categories);

    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("originCategoryId", originCategoryId);
    model.addAttribute("keyword", keyword);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("sort", sort);

    return "admin/gallery/update-form";
  }

  /**
   *
   *
   * <h1>이미지 다운로드 (수정화면)</h1>
   *
   * @param imageId 이미지 번호
   * @param res HttpServletResponse
   */
  @GetMapping("/admin/gallery/image/{imageId}/download")
  public void downloadAttachment(@PathVariable Long imageId, HttpServletResponse res) {

    FileDownloadVo fileDownloadVo = galleryImageService.getImageStoredPath(imageId);

    localFileService.downloadFile(fileDownloadVo, res);
  }

  /**
   *
   *
   * <h1>게시글 수정 </h1>
   *
   * @param req 게시글 수정 폼
   * @param postId 수정할 아이디
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색 키워드
   * @param originCategoryId 기존 카테고리 아이디
   * @param pageSize 페이지 크기
   * @param sort 정렬
   * @param userDetails 인증 정보
   * @return 게시글 수정
   */
  @PostMapping("/admin/gallery/post/{postId}/update")
  public String postUpdate(
      @ModelAttribute ReqUpdateGalleryPost req,
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(postId);

    GalleryUpdateVO galleryUpdateVO =
        galleryService.updatePost(req, galleryPost, userDetails.getId());

    localFileService.saveFile(req.addImages(), galleryUpdateVO.addGalleryImages());

    localFileService.deleteFile(galleryUpdateVO.deleteImages());

    return String.format(
        "redirect:/admin/gallery/post/%d?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
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
   * @param postId 삭제할 아이디
   * @param startDate 시작일
   * @param endDate 종료일
   * @param keyword 검색 키워드
   * @param categoryId 카테고리 번호
   * @param pageSize 페이지 크기
   * @param sort 정렬
   * @param userDetails 인증 정보
   * @return 게시글 목록 화면
   */
  @PostMapping("/admin/gallery/post/{postId}/delete")
  public String deleteGalleryPost(
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(postId);

    List<GalleryImage> deleteGalleryImages =
        galleryService.deletePost(postId, galleryPost, userDetails.getId());

    localFileService.deleteFile(deleteGalleryImages);

    return String.format(
        "redirect:/admin/free/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        startDate,
        endDate,
        categoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }
}
