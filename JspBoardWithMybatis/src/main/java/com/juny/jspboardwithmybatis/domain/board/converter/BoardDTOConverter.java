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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardDTOConverter {

  /**
   *
   *
   * <h1> 게시판 상세 조회 쿼리 결과를 View 에서 사용할 정보로 변환하는 컨버터 </h1>
   *
   * <br>
   * - Map에 담긴 자료를 하나씩 꺼내 ResBoardDetail 변환
   *
   * @param boardId
   * @param rows
   * @return ResBoardDetail
   */
  public static ResBoardDetail convertToResBoardDetail(
      Long boardId, List<Map<String, Object>> rows) {

    Map<String, Object> firstRow = rows.get(0);

    List<ResBoardImage> boardImages =
        rows.stream()
            .filter(row -> row.get("board_image_id") != null)
            .map(
                row ->
                    new ResBoardImage(
                        (Long) row.get("board_image_id"),
                        (String) row.get("stored_name"),
                        (String) row.get("stored_path"),
                        (String) row.get("extension")))
            .collect(Collectors.toList());

    List<ResAttachment> attachments =
        rows.stream()
            .filter(row -> row.get("attachment_id") != null)
            .map(
                row ->
                    new ResAttachment(
                        (Long) row.get("attachment_id"),
                        (String) row.get("logical_name"),
                        (String) row.get("stored_name"),
                        (String) row.get("stored_path"),
                        (String) row.get("extension"),
                        (Integer) row.get("size")))
            .collect(Collectors.toList());

    List<ResComment> comments =
        rows.stream()
            .filter(row -> row.get("comment_id") != null)
            .map(
                row ->
                    new ResComment(
                        (Long) row.get("comment_id"),
                        (String) row.get("content"),
                        (String) row.get("password"),
                        (String) row.get("created_at"),
                        (String) row.get("created_by")))
            .collect(Collectors.toList());

    return new ResBoardDetail(
        boardId,
        (String) firstRow.get("title"),
        (String) firstRow.get("content"),
        (Integer) firstRow.get("view_count"),
        (firstRow.get("created_at") == null)
            ? null
            : DateFormatUtils.toOutputFormat((String) firstRow.get("created_at")),
        (String) firstRow.get("created_by"),
        (firstRow.get("updated_at") == null)
            ? "-"
            : DateFormatUtils.toOutputFormat((String) firstRow.get("updated_at")),
        (String) firstRow.get("name"),
        boardImages,
        attachments,
        comments);
  }

  /**
   *
   *
   * <h1>게시판 목록 조회 쿼리 결과를 View에서 사용할 정보로 변환하는 컨버터</h1>
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
                row ->
                    new ResBoardViewList(
                        (Long) row.get("board_id"),
                        (String) row.get("title"),
                        (Integer) row.get("view_count"),
                        (String) row.get("created_at"),
                        (String) row.get("created_by"),
                        (row.get("updated_at") == null) ? "-" : ((String) row.get("updated_at")),
                        (String) row.get("name"),
                        ((Long) row.get("has_attachment")) > 0))
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
