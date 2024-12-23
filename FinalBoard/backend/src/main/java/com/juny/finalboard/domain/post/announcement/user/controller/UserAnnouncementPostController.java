package com.juny.finalboard.domain.post.announcement.user.controller;

import com.juny.finalboard.domain.post.announcement.common.dto.ReqGetPostList;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostCreate;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqPostUpdate;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.dto.ResAnnouncementPostList;
import com.juny.finalboard.domain.post.announcement.common.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import com.juny.finalboard.domain.post.announcement.common.mapper.AnnouncementPostMapper;
import com.juny.finalboard.domain.post.announcement.common.service.AnnouncementPostService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAnnouncementPostController {

  private final AnnouncementPostService announcementPostService;

  /**
   *
   *
   * <h1>공지사항 게시글 전체 조회 </h1>
   *
   * - 정렬 조건은 created_at:desc, asc 와 view_count:desc, asc 지원
   *
   * @param req 조회 DTO
   * @return 게시글 조회 목록
   */
  @GetMapping("/api/v1/announcement-posts")
  public ResponseEntity<ResAnnouncementPostList> getAnnouncementPosts(
      @ModelAttribute ReqGetPostList req) {

    SearchCondition searchCondition = announcementPostService.createSearchCondition(req);

    long totalBoardCount = announcementPostService.getTotalBoardCount(searchCondition);

    List<AnnouncementPost> postList =
        announcementPostService.getPostListBySearchCondition(searchCondition);

    ResAnnouncementPostList resAnnouncementPostList =
        AnnouncementPostMapper.toResAnnouncementPostList(
            postList, searchCondition, totalBoardCount);

    return new ResponseEntity<>(resAnnouncementPostList, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>공지사항 게시글 단건 조회 </h1>
   *
   * @param postId 게시글 아이디
   * @return 게시글
   */
  @GetMapping("/api/v1/announcement-posts/{postId}")
  public ResponseEntity<ResAnnouncementPost> getAnnouncementPost(@PathVariable Long postId) {

    AnnouncementPost post = announcementPostService.getPostById(postId);

    ResAnnouncementPost resAnnouncementPost = AnnouncementPostMapper.toResAnnouncementPost(post);

    return new ResponseEntity<>(resAnnouncementPost, HttpStatus.OK);
  }
}
