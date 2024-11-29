package com.juny.board.domain.utils;

import com.juny.board.domain.utils.dto.FileDetails;
import com.juny.board.domain.board.entity.FileInfo;
import com.juny.board.domain.utils.dto.ResFileDownload;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 *
 * <h1>파일 다운로드, 저장, 삭제 서비스</h1>
 *
 * <br>
 * - 파일 구현체는 파일시스템에서 저장, 삭제<br>
 * - S3 구현체는 S3에서 저장 삭제
 */
public interface FileService {

  void responseFile(ResFileDownload resFileDownload, HttpServletResponse res);

  List<FileDetails> parseFileDetails(List<MultipartFile> files, String path);

  void saveFile(List<MultipartFile> files, List<? extends FileInfo> fileInfos);

  void deleteFile(List<String> deleteFilePaths);
}
