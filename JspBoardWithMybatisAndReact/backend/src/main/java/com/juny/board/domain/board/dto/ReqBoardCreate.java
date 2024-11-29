package com.juny.board.domain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@ToString
public class ReqBoardCreate implements ReqForm {

  @NotNull(message = "categoryName not null")
  @NotEmpty(message = "categoryName not empty")
  private final String categoryName;

  @NotNull(message = "createdBy not null")
  @NotEmpty(message = "createdBy not empty")
  @Size(min = 3, max = 4, message = "createdBy min 3, max 4")
  private final String createdBy;

  @NotNull(message = "password not null")
  @NotEmpty(message = "password not empty")
  @Size(min = 4, max = 15, message = "password min 4, max 15")
  private final String password;

  @NotNull(message = "passwordConfirm not null")
  @NotEmpty(message = "passwordConfirm not empty")
  @Size(min = 4, max = 15, message = "passwordConfirm min 4, max 15")
  private final String passwordConfirm;

  @NotNull(message = "title not null")
  @NotEmpty(message = "title not empty")
  @Size(min = 4, max = 15, message = "title min 4, max 99")
  private final String title;

  @NotNull(message = "content not null")
  @NotEmpty(message = "content not empty")
  @Size(min = 4, max = 15, message = "content min 4, max 1999")
  private final String content;

  private final List<MultipartFile> images;

  private final List<MultipartFile> attachments;
}
