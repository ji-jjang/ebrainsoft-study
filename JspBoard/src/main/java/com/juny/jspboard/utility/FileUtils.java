package com.juny.jspboard.utility;

import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.BoardImage;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.config.Env;
import com.juny.jspboard.utility.dto.ResFileParsing;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class FileUtils {

  private FileUtils() {}

  /**
   *
   *
   * <h1>첨부 파일과 이미지 파일 처리 </h1>
   *
   * <br>
   * 서버 파일 시스템에 저장하고, 파일 정보들을 DB에 저장하기 위한 파싱 처리
   *
   * @param parts
   * @return 파싱된 이미지 파일과 첨부 파일
   * @throws IOException
   * @throws ServletException
   */
  public static ResFileParsing parsingFiles(Collection<Part> parts) throws IOException {

    List<Attachment> attachments = new ArrayList<>();
    List<BoardImage> images = new ArrayList<>();

    for (var part : parts) {

      if (part.getSize() > 0) {
        String logicalName = part.getSubmittedFileName();
        String extension = Constants.EMPTY_STRING;

        if (!Objects.isNull(logicalName) && logicalName.contains(Constants.DOT_SIGN)) {
          extension = logicalName.substring(logicalName.lastIndexOf(Constants.DOT_SIGN));
          logicalName = logicalName.substring(0, logicalName.lastIndexOf(Constants.DOT_SIGN));
        }

        String uniqueFileName = UUID.randomUUID().toString();
        String filePath;
        if (part.getName().equals(Constants.FILES)) {

          filePath = Env.ATTACHMENT_PATH + File.separator + uniqueFileName + extension;
          part.write(filePath);
          attachments.add(
              new Attachment(
                  logicalName,
                  Constants.EMPTY_STRING,
                  uniqueFileName,
                  Env.ATTACHMENT_PATH,
                  extension,
                  part.getSize(),
                  null));

        } else if (part.getName().equals(Constants.IMAGES)) {

          filePath = Env.IMAGE_PATH + File.separator + uniqueFileName + extension;
          part.write(filePath);

          images.add(new BoardImage(uniqueFileName, Env.IMAGE_PATH, extension, null));
        }
      }
    }

    return new ResFileParsing(attachments, images);
  }
}
