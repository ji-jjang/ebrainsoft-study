package com.juny.board.domain.board.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class ReqBoardPreUpdate implements ReqForm {

  private final String title;
  private final String content;
  private final String password;
  private final String createdBy;

  private final List<Long> deleteImageIds;
  private final List<Long> deleteAttachmentIds;
  private final List<MultipartFile> images;
  private final List<MultipartFile> attachments;

  /**
   *
   *
   * <h1>AllArgsConstructor, 불변 객체로 만들 때 기본값 설정</h1>
   *
   * @param title
   * @param content
   * @param password
   * @param createdBy
   * @param deleteImageIds
   * @param deleteAttachmentIds
   * @param images
   * @param attachments
   */
  public ReqBoardPreUpdate(
      String title,
      String content,
      String password,
      String createdBy,
      List<Long> deleteImageIds,
      List<Long> deleteAttachmentIds,
      List<MultipartFile> images,
      List<MultipartFile> attachments) {

    this.title = title;
    this.content = content;
    this.password = password;
    this.createdBy = createdBy;
    this.deleteImageIds = deleteImageIds != null ? deleteImageIds : new ArrayList<>();
    this.deleteAttachmentIds =
        deleteAttachmentIds != null ? deleteAttachmentIds : new ArrayList<>();
    this.images = images != null ? images : new ArrayList<>();
    this.attachments = attachments != null ? attachments : new ArrayList<>();
  }
}
