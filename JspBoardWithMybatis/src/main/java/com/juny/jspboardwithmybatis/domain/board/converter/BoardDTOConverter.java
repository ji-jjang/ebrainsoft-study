package com.juny.jspboardwithmybatis.domain.board.converter;

import com.juny.jspboardwithmybatis.domain.board.dto.ResAttachment;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardImage;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardList;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardViewList;
import com.juny.jspboardwithmybatis.domain.board.dto.ResComment;
import com.juny.jspboardwithmybatis.domain.board.dto.ResPageInfo;
import com.juny.jspboardwithmybatis.domain.board.dto.ResSearchCondition;
import com.juny.jspboardwithmybatis.domain.utils.DateFormatUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** DAO에서 View 의존성을 없애고, Converter 가 View 에 필요한 정보 변환 */
public class BoardDTOConverter {

  /**
   *
   *
   * <h1>게시판 상세 조회 쿼리 결과를 View 에서 사용할 정보로 변환하는 컨버터 </h1>
   *
   * <br>
   * - Map 담긴 자료를 하나씩 꺼내 ResBoardDetail 변환<br>
   * - 일대다 관계에 있는 엔티티는 GROUP_CONCAT 으로 가져오기 때문에 파싱 로직 필요<br>
   * - 예시 데이터:
   * board_images=79|0de8253db47a49b2b0551b148124e60f|/Users/jijunhyuk/JunyProjects/ebrainsoft/images/|png,80|d9b121fe88c749cf85887eaa0ba01fda|/Users/jijunhyuk/JunyProjects/ebrainsoft/images/|webp
   *
   * @param boardId @Param row
   * @return ResBoardDetail
   */
  public static ResBoardDetail convertToResBoardDetail(Long boardId, Map<String, Object> row) {

    return new ResBoardDetail(
        boardId,
        (String) row.get("board_title"),
        (String) row.get("board_content"),
        (Integer) row.get("board_view_count"),
        (String) row.get("board_created_at"),
        (String) row.get("board_created_by"),
        (String) row.get("board_updated_at"),
        (String) row.get("category_name"),
        parseBoardImages((String) row.get("board_images")),
        parseAttachments((String) row.get("attachments")),
        parseComments((String) row.get("comments")));
  }

  private static List<ResBoardImage> parseBoardImages(String data) {

    if (data == null || data.isEmpty()) return Collections.emptyList();

    return Arrays.stream(data.split(","))
        .map(
            entry -> {
              var tokens = entry.split("\\|");
              return new ResBoardImage(Long.parseLong(tokens[0]), tokens[1], tokens[2], tokens[3]);
            })
        .collect(Collectors.toList());
  }

  private static List<ResAttachment> parseAttachments(String data) {
    if (data == null || data.isEmpty()) return Collections.emptyList();
    return Arrays.stream(data.split(","))
        .map(
            entry -> {
              String[] tokens = entry.split("\\|");
              return new ResAttachment(
                  Long.parseLong(tokens[0]),
                  tokens[1],
                  tokens[2],
                  tokens[3],
                  tokens[4],
                  Integer.parseInt(tokens[5]));
            })
        .collect(Collectors.toList());
  }

  private static List<ResComment> parseComments(String data) {

    if (data == null || data.isEmpty()) return Collections.emptyList();

    return Arrays.stream(data.split(","))
        .map(
            entry -> {
              var tokens = entry.split("\\|");
              return new ResComment(Long.parseLong(tokens[0]), tokens[1], tokens[2], tokens[3]);
            })
        .collect(Collectors.toList());
  }

  /**
   *
   *
   * <h1>게시판 목록 조회 쿼리 결과를 View에서 사용할 정보로 변환하는 컨버터</h1>
   *
   * <br>
   * - EXIST 쿼리 결과를 JDBC 에서 tinyInt 또는 bigInt 로 간헐적으로 랜덤하게 처리되는 문제 발생<br>
   * - InstanceOf로 타입안정성 부여
   *
   * @param boards
   * @param searchConditions
   * @return
   */
  public static ResBoardList convertToResBoardList(
      List<Map<String, Object>> boards, Map<String, Object> searchConditions) {

    List<ResBoardViewList> resBoardViewList =
        boards.stream()
            .map(
                row -> {
                  Long boardId = (Long) row.get("board_id");
                  String title = (String) row.get("title");
                  Integer viewCount = (Integer) row.get("view_count");
                  String createdAt = (String) row.get("created_at");
                  String createdBy = (String) row.get("created_by");
                  String updatedAt =
                      (row.get("updated_at") == null) ? "-" : (String) row.get("updated_at");
                  String name = (String) row.get("name");

                  Object hasAttachmentObj = row.get("has_attachment");
                  boolean hasAttachment =
                      hasAttachmentObj instanceof Number
                          && ((Number) hasAttachmentObj).longValue() > 0;

                  return new ResBoardViewList(
                      boardId,
                      title,
                      viewCount,
                      createdAt,
                      createdBy,
                      updatedAt,
                      name,
                      hasAttachment);
                })
            .collect(Collectors.toList());

    ResSearchCondition resSearchCondition =
        new ResSearchCondition(
            (searchConditions.get("startDate") == null)
                ? null
                : DateFormatUtils.toOutputFormat((String) searchConditions.get("startDate")),
            (searchConditions.get("endDate") == null)
                ? null
                : DateFormatUtils.toOutputFormat((String) searchConditions.get("endDate")),
            (String) searchConditions.get("keyword"),
            (String) searchConditions.get("categoryName"));

    ResPageInfo resPageInfo =
        new ResPageInfo(
            Integer.parseInt(searchConditions.get("totalBoardCount").toString()),
            Integer.parseInt(searchConditions.get("totalPages").toString()),
            Integer.parseInt(searchConditions.get("page").toString()));

    return new ResBoardList(resBoardViewList, resSearchCondition, resPageInfo);
  }
}
