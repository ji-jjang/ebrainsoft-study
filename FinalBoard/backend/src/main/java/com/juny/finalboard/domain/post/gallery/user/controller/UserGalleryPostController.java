package com.juny.finalboard.domain.post.gallery.user.controller;

import com.juny.finalboard.domain.post.common.LocalFileService;
import com.juny.finalboard.domain.post.gallery.common.dto.GallerySearchCondition;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqCreateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqGetGalleryPostList;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqUpdateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ResGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryUpdateVO;
import com.juny.finalboard.domain.post.gallery.common.mapper.GalleryPostMapper;
import com.juny.finalboard.domain.post.gallery.common.service.GalleryService;
import com.juny.finalboard.global.security.common.service.CustomUserDetails;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserGalleryPostController {

  private final GalleryService galleryService;

  private final LocalFileService localFileService;

  /**
   *
   *
   * <h1>갤러리 게시글 생성 </h1>
   *
   * @param req 생성 폼
   * @param userDetails 유저 인증 정보
   * @return 생성된 게시글
   */
  @PostMapping("/v1/gallery-posts")
  public ResponseEntity<ResGalleryPost> createGalleryPost(
      @ModelAttribute ReqCreateGalleryPost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    GalleryPost galleryPost = galleryService.createGalleryPost(req, userDetails.getId());

    ResGalleryPost resGalleryPost = GalleryPostMapper.toResGalleryPost(galleryPost);

    localFileService.saveFile(req.images(), resGalleryPost.galleryImages());

    return new ResponseEntity<>(resGalleryPost, HttpStatus.CREATED);
  }

  /**
   *
   *
   * <h1>갤러리 게시글 상세 조회 </h1>
   *
   * @param postId 게시글 아이디
   * @return 상세 게시글
   */
  @GetMapping("/v1/gallery-posts/{postId}")
  public ResponseEntity<ResGalleryPost> getGalleryPost(@PathVariable Long postId) {

    galleryService.increaseViewCount(postId);

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(postId);

    ResGalleryPost resGalleryPost = GalleryPostMapper.toResGalleryPost(galleryPost);

    return new ResponseEntity<>(resGalleryPost, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>갤러리 게시글 목록 조회 </h1>
   *
   * @param req 목록 조회 시 검색 파라미터
   * @return 게시글 목록
   */
  @GetMapping("/v1/gallery-posts")
  public ResponseEntity<List<ResGalleryPost>> getGalleryPostList(
      @ModelAttribute ReqGetGalleryPostList req) {

    GallerySearchCondition searchCondition = galleryService.createSearchCondition(req);

    long totalPostCount = galleryService.getTotalPostCountBySearchCondition(searchCondition);

    List<GalleryPost> galleryPosts =
        galleryService.getGalleryPostListBySearchCondition(searchCondition);

    List<ResGalleryPost> resGalleryPosts =
        GalleryPostMapper.toResGalleryPostList(galleryPosts, searchCondition, totalPostCount);

    return new ResponseEntity<>(resGalleryPosts, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>갤러리 게시글 수정</h1>
   *
   * @param postId 수정할 게시글 아이디
   * @param req 수정할 게시글 데이터
   * @param userDetails 인증 정보
   * @return 수정된 게시글
   */
  @PatchMapping("/v1/gallery-posts/{postId}")
  public ResponseEntity<ResGalleryPost> updateGalleryPost(
      @PathVariable Long postId,
      @ModelAttribute ReqUpdateGalleryPost req,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(postId);

    GalleryUpdateVO galleryUpdateVO =
        galleryService.updatePost(req, galleryPost, userDetails.getId());

    localFileService.saveFile(req.addImages(), galleryUpdateVO.addGalleryImages());

    localFileService.deleteFile(galleryUpdateVO.deleteImages());

    ResGalleryPost resGalleryPost =
        GalleryPostMapper.toResGalleryPost(galleryUpdateVO.updateGalleryPost());

    return new ResponseEntity<>(resGalleryPost, HttpStatus.OK);
  }

  /**
   *
   *
   * <h1>갤러리 게시글 삭제 </h1>
   *
   * @param postId 삭제할 게시글 아이디
   * @param userDetails 인증 정보
   * @return Void
   */
  @DeleteMapping("/v1/gallery-posts/{postId}")
  public ResponseEntity<Void> deleteGalleryPost(
      @PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {

    GalleryPost galleryPost = galleryService.getGalleryPostDetail(postId);

    List<GalleryImage> deleteGalleryImage =
        galleryService.deletePost(postId, galleryPost, userDetails.getId());

    localFileService.deleteFile(deleteGalleryImage);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
