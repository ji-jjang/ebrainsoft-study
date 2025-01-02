package com.juny.finalboard.domain.post.question.admin.controller;

import com.juny.finalboard.domain.post.question.admin.service.AdminAnswerService;
import com.juny.finalboard.domain.post.question.common.dto.ReqCreateAnswer;
import com.juny.finalboard.domain.post.question.common.dto.ReqGetQuestionPost;
import com.juny.finalboard.domain.post.question.common.dto.ResQuestionPost;
import com.juny.finalboard.domain.post.question.common.entity.QuestionPost;
import com.juny.finalboard.domain.post.question.common.mapper.QuestionPostMapper;
import com.juny.finalboard.domain.post.question.common.service.QuestionService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAnswerController {

  private final QuestionService questionService;

  private final AdminAnswerService adminAnswerService;

  @GetMapping("/question/post/{postId}/answer/create")
  public String getUpdateForm(
    Model model,
    @PathVariable Long postId,
    @RequestParam(required = false) String startDate,
    @RequestParam(required = false) String endDate,
    @RequestParam(required = false) String keyword,
    @RequestParam(required = false) String categoryId,
    @RequestParam(required = false) String pageSize,
    @RequestParam(required = false) String sort,
    @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post = questionService.getQuestionPostDetail(
      postId, ReqGetQuestionPost.builder().build(), userDetails);

    ResQuestionPost resQuestionPost = QuestionPostMapper.toResQuestionPost(post);
    model.addAttribute("post", resQuestionPost);

    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("keyword", keyword);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("sort", sort);

    return "admin/question/answer/create-form";
  }

  @PostMapping("/question/post/{postId}/answer/create")
  public String createAnswer(
      @ModelAttribute ReqCreateAnswer req,
      @PathVariable Long postId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String categoryId,
      @RequestParam(required = false) String pageSize,
      @RequestParam(required = false) String sort,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    QuestionPost post =
        questionService.getQuestionPostDetail(
            postId, ReqGetQuestionPost.builder().build(), userDetails);

    adminAnswerService.createAnswer(req, post, userDetails.getId());

    return String.format(
        "redirect:/admin/question/post/%d?startDate=%s&endDate=%s&categoryId=%s&keyword=%s&pageSize=%s&sort=%s",
        postId,
        startDate,
        endDate,
        categoryId,
        keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "",
        pageSize,
        sort);
  }
}
