package com.juny.finalboard.domain.post.announcement.common.service;

import com.juny.finalboard.domain.post.announcement.common.repository.AnnouncementPostRepository;
import com.juny.finalboard.domain.post.announcement.common.dto.ReqGetPostList;
import com.juny.finalboard.domain.post.announcement.common.dto.SearchCondition;
import com.juny.finalboard.domain.post.announcement.common.entity.AnnouncementPost;
import com.juny.finalboard.global.constant.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementPostService {

  private static final List<String> sortParams = List.of("created_at", "view_count");
  private static final List<String> sortDirections = List.of("asc", "desc");
  private final AnnouncementPostRepository announcementPostRepository;

  /**
   *
   *
   * <h1>게시글 목록 조회시 검색 조건 생성 </h1>
   *
   * @param req 조회 DTO
   * @return 검색 조건
   */
  public SearchCondition createSearchCondition(ReqGetPostList req) {

    String[] sort = req.sort().split(":");

    if (sort.length != 2) {
      throw new RuntimeException("invalid sort params" + req.sort());
    }
    if (!sortParams.contains(sort[0]) || !sortDirections.contains(sort[1])) {
      throw new RuntimeException(String.format("not support sort %s %s", sort[0], sort[1]));
    }

    return SearchCondition.builder()
        .startDate(req.startDate() + " 00:00:00")
        .endDate(req.endDate() + " 23:59:59")
        .categoryId(req.categoryId())
        .keyword(req.keyword())
        .sort(sort[0] + Constants.SPACE_SIGN + sort[1])
        .page(req.page() - 1)
        .pageSize(req.pageSize())
        .offset((req.page() - 1) * req.pageSize())
        .build();
  }

  /**
   *
   *
   * <h1>게시글 목록 조회 시 검색 조건에 따른 총 게시글 수 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 총 게시글 수
   */
  public long getTotalBoardCount(SearchCondition searchCondition) {

    return announcementPostRepository.getTotalAnnouncementPostCount(searchCondition);
  }

  /**
   *
   *
   * <h1>게시글 목록 조회 </h1>
   *
   * @param searchCondition 검색 조건
   * @return 게시글 목록
   */
  public List<AnnouncementPost> getPostListBySearchCondition(SearchCondition searchCondition) {

    return announcementPostRepository.findAllWithCategoryBySearchCondition(searchCondition);
  }

  /**
   *
   *
   * <h1>게시글 단건 조회 </h1>
   *
   * @param id 게시글 ID
   * @return 게시글
   */
  public AnnouncementPost getPostById(Long id) {

    return announcementPostRepository
            .findPostDetailById(id)
            .orElseThrow(() -> new RuntimeException(String.format("invalid post id: %d", id)));
  }

}
