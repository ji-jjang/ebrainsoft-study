package com.juny.jspboardwithmybatis.domain.board.converter;

import com.juny.jspboardwithmybatis.domain.board.dto.ResAttachment;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardDetail;
import com.juny.jspboardwithmybatis.domain.board.dto.ResBoardImage;
import com.juny.jspboardwithmybatis.domain.board.dto.ResComment;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardDTOConverter {

  /**
   * <h1> DAO 결과를 View 에서 사용할 정보로 변환하는 컨버터 </h1>
   * <br>- Map에 담긴 자료를 하나씩 꺼내 ResBoardDetail 변환
   * @param boardId
   * @param rows
   * @return ResBoardDetail
   */
  public static ResBoardDetail convertToResBoardDetail(Long boardId, List<Map<String, Object>> rows) {

    Map<String, Object> firstRow = rows.get(0);

    List<ResBoardImage> boardImages =
        rows.stream()
            .filter(row -> row.get("boardImageId") != null)
            .map(
                row ->
                    new ResBoardImage(
                        (Long) row.get("boardImageId"),
                        (String) row.get("storedName"),
                        (String) row.get("storedPath"),
                        (String) row.get("extension")))
            .collect(Collectors.toList());

    List<ResAttachment> attachments =
        rows.stream()
            .filter(row -> row.get("attachmentId") != null)
            .map(
                row ->
                    new ResAttachment(
                        (Long) row.get("attachmentId"),
                        (String) row.get("logicalName"),
                        (String) row.get("storedName"),
                        (String) row.get("storedPath"),
                        (String) row.get("extension"),
                        (Integer) row.get("size")))
            .collect(Collectors.toList());

    List<ResComment> comments =
        rows.stream()
            .filter(row -> row.get("commentId") != null)
            .map(
                row ->
                    new ResComment(
                        (Long) row.get("commentId"),
                        (String) row.get("content"),
                        (String) row.get("password"),
                        (String) row.get("createdAt"),
                        (String) row.get("createdBy")))
            .collect(Collectors.toList());

    return new ResBoardDetail(
        boardId,
        (String) firstRow.get("title"),
        (String) firstRow.get("content"),
        (Integer) firstRow.get("viewCount"),
        (String) firstRow.get("createdAt"),
        (String) firstRow.get("createdBy"),
        (String) firstRow.get("updatedAt"),
        (String) firstRow.get("categoryName"),
        boardImages,
        attachments,
        comments);
  }
}
