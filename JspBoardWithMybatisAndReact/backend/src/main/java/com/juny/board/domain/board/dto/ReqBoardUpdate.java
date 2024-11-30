package com.juny.board.domain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@ToString
public class ReqBoardUpdate implements ReqForm {

  @NotNull(message = "title not null")
  @NotEmpty(message = "title not empty")
  @Size(min = 4, max = 15, message = "title min 4, max 99")
  private String title;

  @NotNull(message = "content not null")
  @NotEmpty(message = "content not empty")
  @Size(min = 4, max = 15, message = "content min 4, max 1999")
  private String content;

  @NotNull(message = "password not null")
  @NotEmpty(message = "password not empty")
  @Size(min = 4, max = 15, message = "password min 4, max 15")
  private String password;

  @NotNull(message = "createdBy not null")
  @NotEmpty(message = "createdBy not empty")
  @Size(min = 4, max = 15, message = "createdBy min 3, max 4")
  private String createdBy;

  private List<Long> deleteImageIds;
  private List<Long> deleteAttachmentIds;
  private List<MultipartFile> addImages;
  private List<MultipartFile> addAttachments;

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
   * @param addImages
   * @param addAttachments
   */
  public ReqBoardUpdate(
      String title,
      String content,
      String password,
      String createdBy,
      List<Long> deleteImageIds,
      List<Long> deleteAttachmentIds,
      List<MultipartFile> addImages,
      List<MultipartFile> addAttachments) {

    this.title = title;
    this.content = content;
    this.password = password;
    this.createdBy = createdBy;
    this.deleteImageIds = deleteImageIds != null ? deleteImageIds : new ArrayList<>();
    this.deleteAttachmentIds =
        deleteAttachmentIds != null ? deleteAttachmentIds : new ArrayList<>();
    this.addImages = addImages != null ? addImages : new ArrayList<>();
    this.addAttachments = addAttachments != null ? addAttachments : new ArrayList<>();
  }
}
