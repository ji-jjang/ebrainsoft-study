package com.juny.board.domain.board.entity.vo;

import com.juny.board.domain.utils.dto.FileDetails;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateVO {

  private List<Long> deleteImageIds;
  private List<Long> deleteAttachmentIds;
  private List<String> deleteFilePaths;
  private List<FileDetails> addImageDetails;
  private List<FileDetails> addAttachmentDetails;
}
