package com.juny.jspboard.utility;

import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.BoardImage;
import com.juny.jspboard.constant.Constants;
import com.juny.jspboard.constant.Env;
import com.juny.jspboard.utility.dto.ResDeleteFileParsing;
import com.juny.jspboard.utility.dto.ResFileParsing;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class FileUtils {

  private FileUtils() {}

  /**
   * 첨부 파일과 이미지 파일을 서버 파일 시스템에 저장하고, 파일 정보들을 DB에 저장하기 위한 파싱 처리
   *
   * @param req
   * @return
   * @throws IOException
   * @throws ServletException
   */
  public static ResFileParsing parsingFiles(HttpServletRequest req)
      throws IOException, ServletException {

    List<Attachment> attachments = new ArrayList<>();
    List<BoardImage> images = new ArrayList<>();

    for (var part : req.getParts()) {

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
                  part.getSize()));

        } else if (part.getName().equals(Constants.IMAGES)) {

          filePath = Env.IMAGE_PATH + File.separator + uniqueFileName + extension;
          part.write(filePath);

          images.add(new BoardImage(uniqueFileName, Env.IMAGE_PATH, extension));
        }
      }
    }

    return new ResFileParsing(attachments, images);
  }

  public static ResDeleteFileParsing parseDeleteFilePaths(HttpServletRequest req) {

    String[] attachmentsFilePath = req.getParameterValues(Constants.ATTACHMENT_FILE_PATH);
    String[] attachmentsStoredName = req.getParameterValues(Constants.ATTACHMENT_STORED_NAME);
    String[] attachmentExtensions = req.getParameterValues(Constants.ATTACHMENT_EXTENSION);

    String[] imageFilePath = req.getParameterValues(Constants.IMAGE_FILE_PATH);
    String[] imageStoredName = req.getParameterValues(Constants.IMAGE_STORED_NAME);
    String[] imageExtensions = req.getParameterValues(Constants.IMAGE_EXTENSION);

    List<String> deleteImages = createFilePaths(imageFilePath, imageStoredName, imageExtensions);
    List<String> deleteAttachments =
        createFilePaths(attachmentsFilePath, attachmentsStoredName, attachmentExtensions);
    String[] comments = req.getParameterValues(Constants.COMMENT_ID);
    List<String> deleteComments = null;
    if (comments != null && comments.length > 0) {
        deleteComments = Arrays.stream(req.getParameterValues(Constants.COMMENT_ID)).toList();
    }

    return new ResDeleteFileParsing(deleteImages, deleteAttachments, deleteComments);
  }

  private static List<String> createFilePaths(
      String[] filePaths, String[] storedNames, String[] extensions) {
    List<String> result = new ArrayList<>();
    if (storedNames != null) {
      for (int i = 0; i < storedNames.length; ++i) {
        String filePath = filePaths[i] + storedNames[i] + extensions[i];
        result.add(filePath);
      }
    }
    return result;
  }
}
