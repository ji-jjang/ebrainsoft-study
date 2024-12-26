package com.juny.finalboard.domain.post.free.common.service;

import com.juny.finalboard.domain.post.free.common.dto.FreeSearchCondition;
import com.juny.finalboard.domain.post.free.common.dto.FreeUpdateVO;
import com.juny.finalboard.domain.post.free.common.dto.ReqCreateFreePost;
import com.juny.finalboard.domain.post.free.common.dto.ReqGetFreePostList;
import com.juny.finalboard.domain.post.free.common.dto.ReqUpdateFreePost;
import com.juny.finalboard.domain.post.free.common.entity.FreeAttachment;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import com.juny.finalboard.domain.post.free.common.entity.FreePostCategory;
import com.juny.finalboard.domain.post.free.common.repository.FreeAttachmentRepository;
import com.juny.finalboard.domain.post.free.common.repository.FreeCommentRepository;
import com.juny.finalboard.domain.post.free.common.repository.FreePostCategoryRepository;
import com.juny.finalboard.domain.post.free.common.repository.FreePostRepository;
import com.juny.finalboard.domain.user.common.User;
import com.juny.finalboard.domain.user.common.UserRepository;
import com.juny.finalboard.global.constant.Constants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FreePostService {

  private static final List<String> sortParams = List.of("created_at", "view_count");
  private static final List<String> sortDirections = List.of("asc", "desc");
  private final UserRepository userRepository;
  private final FreePostRepository freePostRepository;
  private final FreePostCategoryRepository freePostCategoryRepository;
  private final FreeAttachmentRepository freeAttachmentRepository;
  private final FreeCommentRepository freeCommentRepository;

  @Value("${resources.attachment-resource}")
  private String attachmentResourcePath;

  /**
   *
   *
   * <h1>자유 게시글 생성 </h1>
   *
   * @param req ReqCreateFreePost
   * @param userId 유저 아이디
   * @return FreePost
   */
  @Transactional
  public FreePost createFreePost(ReqCreateFreePost req, Long userId) {

    User user = getUserByUserId(userId);

    FreePostCategory freePostCategory =
        freePostCategoryRepository
            .findById(Long.parseLong(req.categoryId()))
            .orElseThrow(
                () ->
                    new RuntimeException(
                        String.format("invalid category id: %s", req.categoryId())));

    FreePost freePost =
        FreePost.builder()
            .title(req.title())
            .content(req.content())
            .createdBy(user.getName())
            .createdAt(LocalDateTime.now())
            .freePostCategory(freePostCategory)
            .build();

    freePostRepository.save(freePost);

    List<FreeAttachment> attachmentList = new ArrayList<>();
    saveAttachment(req.attachments(), freePost.getId(), attachmentList);

    return freePost.toBuilder().freeAttachmentList(attachmentList).build();
  }

  private void saveAttachment(
      List<MultipartFile> attachments, Long postId, List<FreeAttachment> attachmentList) {

    for (var att : attachments) {
      long size = att.getSize();
      String logicalName = att.getOriginalFilename();
      String extension = "";
      String storedName = UUID.randomUUID().toString().replaceAll("-", "");
      if (logicalName != null && logicalName.contains(".")) {
        extension = logicalName.substring(logicalName.lastIndexOf("."));
      }
      if (!(extension.equals(".jpg")
          || extension.equals(".gif")
          || extension.equals(".png")
          || extension.equals(".zip"))) {
        throw new RuntimeException(String.format("invalid extension: %s", extension));
      }
      FreeAttachment freeAttachment =
          FreeAttachment.builder()
              .logicalName(logicalName)
              .storedPath(attachmentResourcePath)
              .storedName(storedName)
              .extension(extension)
              .size(size)
              .freePost(FreePost.builder().id(postId).build())
              .build();

      freeAttachmentRepository.save(freeAttachment);
      attachmentList.add(freeAttachment);
    }
  }

  /**
   *
   *
   * <h1>자유게시글 상세 조회 (카테고리, 첨부파일 , 댓글 조인)</h1>
   *
   * @param postId 게시글 아이디
   * @return 게시글 상세 정보
   */
  public FreePost getFreePostDetail(Long postId) {

    return freePostRepository
        .findFreePostDetailById(postId)
        .orElseThrow(() -> new RuntimeException(String.format("invalid post id: %d", postId)));
  }

  /**
   *
   *
   * <h1>자유게시글 목록 조회 시 검색 조건 생성</h1>
   *
   * @param req 목록 조회 요청 DTO
   * @return 검색 조건
   */
  public FreeSearchCondition createSearchCondition(ReqGetFreePostList req) {

    String[] sort = req.sort().split(Constants.COLON_SIGN);

    if (sort.length != 2) {
      throw new RuntimeException("invalid sort params" + req.sort());
    }
    if (!sortParams.contains(sort[0]) || !sortDirections.contains(sort[1])) {
      throw new RuntimeException(String.format("not support sort %s %s", sort[0], sort[1]));
    }

    return FreeSearchCondition.builder()
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
   * <h1>검색 조건에 따른 전체 게시글 수</h1>
   *
   * @param searchCondition 검색 조건
   * @return 전체 게시글 수
   */
  public long getTotalPostCount(FreeSearchCondition searchCondition) {

    return freePostRepository.getTotalFreePostCount(searchCondition, -1);
  }

  /**
   *
   *
   * <h1>검색 조건에 따른 게시물 목록 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 게시글 목록
   */
  public List<FreePost> getFreePostListBySearchCondition(FreeSearchCondition searchCondition) {

    int offset = searchCondition.page() * searchCondition.pageSize();

    return freePostRepository.findAllWithBySearchCondition(searchCondition, offset);
  }

  /**
   *
   *
   * <h1>게시글 수정 </h1>
   *
   * @param req 수정 폼
   * @param freePost 수정 전 게시글
   * @param userId 사용자 아이디
   * @return 수정된 게시글
   */
  @Transactional
  public FreeUpdateVO updatePost(ReqUpdateFreePost req, FreePost freePost, Long userId) {

    User user = getUserByUserId(userId);

    if (user.getRole().equals(Constants.USER_ROLE)) {
      if (!freePost.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    if (req.deleteAttachmentIds() != null && !req.deleteAttachmentIds().isEmpty()) {
      List<Long> postAttachmentIds =
          freePost.getFreeAttachmentList().stream().map(FreeAttachment::getId).toList();

      boolean isValid = postAttachmentIds.containsAll(req.deleteAttachmentIds());

      if (!isValid) throw new RuntimeException("cannot delete unlinked attachment.");
    }

    FreePost updateFreePost =
        freePost.toBuilder()
            .id(freePost.getId())
            .title(req.title())
            .content(req.content())
            .freePostCategory(
                FreePostCategory.builder().id(Long.parseLong(req.categoryId())).build())
            .build();

    freePostRepository.updatePost(updateFreePost);

    List<FreeAttachment> addAttachmentList = new ArrayList<>();
    saveAttachment(req.addAttachments(), updateFreePost.getId(), addAttachmentList);

    List<FreeAttachment> deleteAttachmentList = deleteAttachmentAndStoreFileInfo(req);

    return new FreeUpdateVO(
        updateFreePost.toBuilder().freeAttachmentList(addAttachmentList).build(),
        addAttachmentList,
        req.addAttachments(),
        deleteAttachmentList);
  }

  private List<FreeAttachment> deleteAttachmentAndStoreFileInfo(ReqUpdateFreePost req) {
    List<FreeAttachment> deleteAttachmentList = new ArrayList<>();
    for (var attId : req.deleteAttachmentIds()) {
      FreeAttachment attachment =
          freeAttachmentRepository
              .findById(attId)
              .orElseThrow(
                  () -> new RuntimeException(String.format("invalid attachment id: %s", attId)));
      deleteAttachmentList.add(attachment);
      freeAttachmentRepository.deleteById(attId);
    }
    return deleteAttachmentList;
  }

  /**
   *
   *
   * <h1>게시글 삭제 </h1>
   *
   * @param postId
   * @param freePost
   * @param userId
   */
  public void deletePostById(Long postId, FreePost freePost, Long userId) {

    User user = getUserByUserId(userId);

    if (user.getRole().equals(Constants.USER_ROLE)) {
      if (!freePost.getUser().getId().equals(userId)) {
        throw new RuntimeException("user is not allowed to update (post's user id does not match)");
      }
    }

    for (var att : freePost.getFreeAttachmentList()) {
      freeAttachmentRepository.deleteById(att.getId());
    }

    for (var comment : freePost.getFreeCommentList()) {
      freeCommentRepository.deleteCommentById(comment.getId());
    }

    freePostRepository.deleteById(postId);
  }

  private User getUserByUserId(Long userId) {

    return userRepository
        .findById(userId)
        .orElseThrow(() -> new RuntimeException(String.format("invalid user id: %d", userId)));
  }
}
