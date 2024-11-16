package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.Board;
import com.juny.jspboard.board.entity.BoardImage;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface BoardDAO {

  Long getCategoryIdByName(String category);

  Long saveBoard(Connection conn, Board board);

  void saveBoardImage(Connection conn, BoardImage boardImage);

  void saveAttachment(Connection conn, Attachment attachment);

  List<String> findImagePathsByBoardId(Connection conn, Long boardId);

  List<String> findAttachmentPathsByBoardId(Connection conn, Long boardId);

  void deleteCommentsByBoardId(Connection conn, Long boardId);

  void deleteAttachmentsByBoardId(Connection conn, Long boardId);

  void deleteImagesByBoardId(Connection conn, Long boardId);

  void deleteBoard(Connection conn, Long boardId);

  void increaseViewCount(Connection conn, Long boardId);

  Board getBoardDetail(Connection conn, Long boardId);

  Board getBoardDetail(Long boardId);

  int getTotals(Map<String, String> searchConditions);

  List<Board> getBoardList(int page, Map<String, String> searchConditions);

  void updateBoard(Connection conn, Board board);

  String getStoredPassword(Long boardId);
}
