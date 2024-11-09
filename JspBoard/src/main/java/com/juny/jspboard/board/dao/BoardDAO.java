package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.dto.ReqBoardUpdate;
import com.juny.jspboard.board.dto.ResBoardDetail;
import com.juny.jspboard.board.dto.ResBoardViewList;
import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.Board;
import com.juny.jspboard.board.entity.BoardImage;
import java.util.List;
import java.util.Map;

public interface BoardDAO {

  int getTotalsWithSearchConditions(Map<String, String> searchConditions);

  List<ResBoardViewList> getBoardSearchList(int page, Map<String, String> searchConditions);

  int getTotals();

  List<ResBoardViewList> getBoardList(int page);

  Long getCategoryIdByName(String category);

  ResBoardDetail getBoardDetail(Long boardId);

  ResBoardDetail updateBoard(ReqBoardUpdate reqBoardUpdate);

  void deleteBoard(
      Long boardId, String[] deleteImages, String[] deleteAttachments, List<Long> deleteCommentsId);

  Long createBoard(
      String category, Board board, List<BoardImage> images, List<Attachment> attachments);

  String getStoredPassword(Long boardId);

  void createComment(Long boardId, String name, String password, String content);
}
