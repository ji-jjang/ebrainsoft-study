package com.juny.finalboard.domain.post.gallery.common.service;

import static java.util.stream.Collectors.groupingBy;

import com.juny.finalboard.domain.post.gallery.common.dto.GallerySearchCondition;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqCreateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqGetGalleryPostList;
import com.juny.finalboard.domain.post.gallery.common.dto.ReqUpdateGalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryCategory;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryPost;
import com.juny.finalboard.domain.post.gallery.common.entity.GalleryUpdateVO;
import com.juny.finalboard.domain.post.gallery.common.repository.GalleryCategoryRepository;
import com.juny.finalboard.domain.post.gallery.common.repository.GalleryImageRepository;
import com.juny.finalboard.domain.post.gallery.common.repository.GalleryPostRepository;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.domain.user.common.UserRepository;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GalleryService {

  private static final List<String> sortParams = List.of("created_at", "view_count");
  private static final List<String> sortDirections = List.of("asc", "desc");
  private final UserRepository userRepository;
  private final GalleryPostRepository galleryPostRepository;
  private final GalleryCategoryRepository galleryCategoryRepository;
  private final GalleryImageRepository galleryImageRepository;

  @Value("${resources.image-resource}")
  private String imageResourcePath;

  /**
   *
   *
   * <h1>갤러리 게시글 생성 </h1>
   *
   * <br>
   * - 갤러리 게시글, 이미지 DB 저장
   *
   * @param req 갤러리 게시글 생성 폼
   * @param userId 유저 아이디
   * @return 생성된 게시글
   */
  @Transactional
  public GalleryPost createGalleryPost(ReqCreateGalleryPost req, Long userId) {

    User user = getUser(userId);

    GalleryCategory galleryCategory = getGalleryCategory(req.categoryId());

    GalleryPost galleryPost =
        GalleryPost.builder()
            .title(req.title())
            .content(req.content())
            .viewCount(0)
            .createdAt(LocalDateTime.now())
            .createdBy(user.getName())
            .galleryCategory(galleryCategory)
            .user(user)
            .build();

    galleryPostRepository.save(galleryPost);

    List<GalleryImage> galleryImages = new ArrayList<>();
    saveImages(req.images(), galleryPost.getId(), galleryImages);

    return galleryPost;
  }

  /**
   *
   *
   * <h1>게시글 상세 조회 시 조회수 증가 </h1>
   *
   * @param postId 게시글 아이디
   */
  public void increaseViewCount(Long postId) {

    galleryPostRepository.increaseViewCount(postId);
  }

  /**
   *
   *
   * <h1>게시글 상세 조회 </h1>
   *
   * <br>
   * - 관련 엔티티 함께 조회(유저, 카테고리, 이미지)
   *
   * @param postId 게시글 아이디
   * @return 조회된 게시글
   */
  public GalleryPost getGalleryPostDetail(Long postId) {

    return galleryPostRepository
        .findGalleryPostDetailById(postId)
        .orElseThrow(
            () -> new RuntimeException(String.format("post not found postId: %d", postId)));
  }

  /**
   *
   *
   * <h1>목록 조회 시 검색 조건 생성 </h1>
   *
   * @param req 목록 조회 시 검색 파라미터
   * @return 검색 조건
   */
  public GallerySearchCondition createSearchCondition(ReqGetGalleryPostList req) {

    String[] sort = req.sort().split(Constants.COLON_SIGN);

    if (sort.length != 2) {
      throw new RuntimeException("invalid sort params" + req.sort());
    }
    if (!sortParams.contains(sort[0]) || !sortDirections.contains(sort[1])) {
      throw new RuntimeException(String.format("not support sort %s %s", sort[0], sort[1]));
    }

    return GallerySearchCondition.builder()
        .startDate(req.startDate() + " 00:00:00")
        .endDate(req.endDate() + " 23:59:59")
        .categoryId(req.categoryId())
        .keyword(req.keyword())
        .sort(sort[0] + Constants.SPACE_SIGN + sort[1])
        .page(req.page() - 1)
        .pageSize(req.pageSize())
        .build();
  }

  /**
   *
   *
   * <h1>검색 조건으로 게시글 전체 개수 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 검색된 게시글 전체 수
   */
  public long getTotalPostCountBySearchCondition(GallerySearchCondition searchCondition) {

    return galleryPostRepository.getTotalGalleryPostCountBySearchCondition(searchCondition, -1);
  }

  /**
   *
   *
   * <h1>검색 조건으로 게시글 목록 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 조회된 페이지 게시글 목록
   */
  public List<GalleryPost> getGalleryPostListBySearchCondition(
      GallerySearchCondition searchCondition) {

    int offset = searchCondition.page() * searchCondition.pageSize();

    List<GalleryPost> galleryPosts =
        galleryPostRepository.findAllWithBySearchCondition(searchCondition, offset);

    List<Long> postIds = galleryPosts.stream().map(GalleryPost::getId).toList();

    List<GalleryImage> galleryImages = galleryImageRepository.findImagesByPostIds(postIds);

    Map<Long, List<GalleryImage>> imagesByPostId =
        galleryImages.stream().collect(groupingBy(image -> image.getPost().getId()));

    return galleryPosts.stream()
        .map(
            galleryPost ->
                galleryPost.toBuilder()
                    .galleryImages(
                        imagesByPostId.getOrDefault(galleryPost.getId(), Collections.emptyList()))
                    .build())
        .toList();
  }

  /**
   *
   *
   * <h1>게시글 수정 </h1>
   *
   * @param req 수정 폼
   * @param galleryPost 수정하기 전 게시글 조회
   * @param userId 유저 아이디
   * @return 수정된 게시글
   */
  @Transactional
  public GalleryUpdateVO updatePost(
      ReqUpdateGalleryPost req, GalleryPost galleryPost, Long userId) {

    User user = getUser(userId);

    GalleryCategory galleryCategory = getGalleryCategory(req.categoryId());

    if (user.getRole().equals(Constants.USER_ROLE)) {
      if (!galleryPost.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    if (req.deleteImageIds() != null && !req.deleteImageIds().isEmpty()) {
      List<Long> postImageIds =
          galleryPost.getGalleryImages().stream().map(GalleryImage::getId).toList();

      boolean isValid = postImageIds.containsAll(req.deleteImageIds());

      if (!isValid) throw new RuntimeException("cannot delete unlinked image.");
    }

    GalleryPost updateGalleryPost =
        galleryPost.toBuilder()
            .title(req.title())
            .content(req.content())
            .galleryCategory(galleryCategory)
            .build();

    galleryPostRepository.updatePost(updateGalleryPost);

    List<GalleryImage> addGalleryImages = new ArrayList<>();
    saveImages(req.addImages(), updateGalleryPost.getId(), addGalleryImages);

    List<GalleryImage> deleteImages = deleteImagesAndStoreFileInfo(req);

    return new GalleryUpdateVO(updateGalleryPost, addGalleryImages, deleteImages);
  }

  private List<GalleryImage> deleteImagesAndStoreFileInfo(ReqUpdateGalleryPost req) {

    List<GalleryImage> deleteImages = new ArrayList<>();
    for (var imageId : req.deleteImageIds()) {

      GalleryImage galleryImage =
          galleryImageRepository
              .findById(imageId)
              .orElseThrow(
                  () -> new RuntimeException(String.format("invalid image id: %s", imageId)));

      deleteImages.add(galleryImage);
      galleryImageRepository.deleteById(imageId);
    }
    return deleteImages;
  }

  /**
   *
   *
   * <h1>게시글 삭제 </h1>
   *
   * <br>
   * - 일대다 연관된 엔티티인 이미지 삭제 후 게시글 삭제
   *
   * @param postId 게시글 아이디
   * @param galleryPost 게시글
   * @param userId 유저 아이디
   * @return 이미지 파일 경로 (localFileService 이미지 파일 삭제)
   */
  @Transactional
  public List<GalleryImage> deletePost(Long postId, GalleryPost galleryPost, Long userId) {

    User user = getUser(userId);

    if (user.getRole().equals(Constants.USER_ROLE)) {
      if (!galleryPost.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    List<Long> deleteImageIds =
        galleryPost.getGalleryImages().stream().map(GalleryImage::getId).toList();

    List<GalleryImage> galleryImages =
        deleteImagesAndStoreFileInfo(
            ReqUpdateGalleryPost.builder().deleteImageIds(deleteImageIds).build());

    galleryPostRepository.deletePostById(postId);

    return galleryImages;
  }

  private User getUser(Long userId) {

    return userRepository
        .findById(userId)
        .orElseThrow(
            () -> new RuntimeException(String.format("user not found userId: %d", userId)));
  }

  private GalleryCategory getGalleryCategory(String req) {

    return galleryCategoryRepository
        .findById(Long.parseLong(req))
        .orElseThrow(
            () -> new RuntimeException(String.format("category not found categoryId: %s", req)));
  }

  private void saveImages(
      List<MultipartFile> images,
      Long postId,
      List<com.juny.finalboard.domain.post.gallery.common.entity.GalleryImage> galleryImages) {

    if (images.isEmpty()) {
      return;
    }

    for (var image : images) {
      long size = image.getSize();
      String logicalName = image.getOriginalFilename();
      String extension = "";
      String storedName = UUID.randomUUID().toString().replaceAll("-", "");
      if (logicalName != null && logicalName.contains(".")) {
        extension = logicalName.substring(logicalName.lastIndexOf("."));
      }

      GalleryImage galleryImage =
          GalleryImage.builder()
              .logicalName(logicalName)
              .storedPath(imageResourcePath)
              .storedName(storedName)
              .extension(extension)
              .size(size)
              .post(GalleryPost.builder().id(postId).build())
              .build();

      galleryImageRepository.save(galleryImage);
      galleryImages.add(galleryImage);
    }
  }
}
