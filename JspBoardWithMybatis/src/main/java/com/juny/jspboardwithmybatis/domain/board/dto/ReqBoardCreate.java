package com.juny.jspboardwithmybatis.domain.board.dto;

import com.juny.jspboardwithmybatis.domain.utils.dto.FileDetails;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@ToString
public class ReqBoardCreate {

  private final String categoryName;
  private final String createdBy;
  private final String password;
  private final String passwordConfirm;
  private final String title;
  private final String content;
  private final List<MultipartFile> images;
  private final List<MultipartFile> attachments;
  private final List<FileDetails> imageDetails;
  private final List<FileDetails> attachmentDetails;
}
