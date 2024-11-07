package com.juny.jspboard.dao;

import com.juny.jspboard.dto.ResBoardDetail;
import com.juny.jspboard.dto.ResBoardViewList;
import com.juny.jspboard.entity.Attachment;
import com.juny.jspboard.entity.Board;
import com.juny.jspboard.entity.BoardImage;
import java.util.List;
import java.util.Map;

public interface BoardDAO {

  List<ResBoardViewList> getBoardList(int page);
  ResBoardDetail getBoardDetail(int boardId);
  ResBoardViewList updateBoard(int boardId);
  void deleteBoard(int boardId);

  List<ResBoardViewList> getBoardSearchList(int page, Map<String, String> searchConditions);
  List<String> getCategories();

  int getTotals();

  int getTotalsWithSearchConditions(Map<String, String> searchConditions);

  void increaseViewCount(int boardId);

  void createComment(int boardId, String name, String password, String content);

  Long getCategoryIdByName(String category);

  Long createBoard(String category, Board board, List<BoardImage> images, List<Attachment> attachments);
}

