package com.juny.finalboard.domain.post.question.admin.controller;

import com.juny.finalboard.domain.post.question.common.dto.QuestionSearchCondition;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqDeleteQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPostList;
import com.juny.finalboard.domain.post.question.common.dto.ReqUpdateQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPostList;
import com.juny.finalboard.domain.post.question.common.entity.QuestionCategory;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.mapper.QuestionPostMapper;
import com.juny.finalboard.domain.post.question.common.service.QuestionCategoryService;
import com.juny.finalboard.domain.post.question.common.service.QuestionService;
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
public class AdminQuestionPostController {

  private final QuestionService questionService;

  private final QuestionCategoryService questionCategoryService;

  @PostMapping("/admin/question/create")
  public String createQuestionPost(
      @ModelAttribute @Valid ReqCreateQuestionPost req,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    questionService.createQuestionPost(req, userDetails.getId());

    return String.format(
        "redirect:/admin/question/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        startDate,
        endDate,
        originCategoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }

  @GetMapping("/admin/question/board")
  public String getQuestionBoard(Model model, @ModelAttribute ReqGetQuestionPostList req) {

    QuestionSearchCondition searchCondition = questionService.createSearchCondition(req);

    long totalPostCount = questionService.getTotalPostCountBySearchCondition(searchCondition);

    List<QuestionPost> postList =
        questionService.getQuestionPostListBySearchCondition(searchCondition);

    ResQuestionPostList resQuestionPostList =
        QuestionPostMapper.toResQuestionPostList(postList, searchCondition, totalPostCount);

    List<QuestionCategory> categories = questionCategoryService.getAllCategories();

    model.addAttribute("categories", categories);
    model.addAttribute("postList", resQuestionPostList.postList());
    model.addAttribute("searchCondition", resQuestionPostList.searchCondition());
    model.addAttribute("pageInfo", resQuestionPostList.pageInfo());

    return "admin/question/board";
  }

  @GetMapping("/admin/question/post/{postId}")
  public String getQuestionPost(
      Model model,
      @PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().build(), userDetails);

    List<QuestionCategory> categories = questionCategoryService.getAllCategories();

    ResQuestionPost resQuestionPost = QuestionPostMapper.toResQuestionPost(post);

    model.addAttribute("categories", categories);

    model.addAttribute("post", resQuestionPost);

    return "admin/question/post";
  }

  @GetMapping("/admin/question/create-form")
  public String getCreateForm(Model model) {

    List<QuestionCategory> categories = questionCategoryService.getAllCategories();

    model.addAttribute("categories", categories);

    return "admin/question/create-form";
  }

  @GetMapping("/admin/question/post/{postId}/update-form")
  public String getUpdateForm(
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      Model model) {

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().build(), userDetails);

    ResQuestionPost resQuestionPost = QuestionPostMapper.toResQuestionPost(post);

    List<QuestionCategory> categories = questionCategoryService.getAllCategories();

    model.addAttribute("post", resQuestionPost);
    model.addAttribute("categories", categories);

    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("originCategoryId", originCategoryId);
    model.addAttribute("keyword", keyword);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("sort", sort);

    return "admin/question/update-form";
  }

  @PostMapping("/admin/question/post/{postId}/update")
  public String postUpdate(
      @ModelAttribute ReqUpdateQuestionPost req,
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String originCategoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().build(), userDetails);

    questionService.updatePost(req, post, userDetails);

    return String.format(
        "redirect:/admin/question/post/%d?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        postId,
        startDate,
        endDate,
        originCategoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }

  @PostMapping("/admin/question/post/{postId}/delete")
  public String deleteQuestionPost(
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @ModelAttribute ReqDeleteQuestionPost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().password(req.password()).build(), userDetails);

    questionService.deletePost(req, post, userDetails);

    return String.format(
        "redirect:/admin/question/board?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        startDate,
        endDate,
        categoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }
}
