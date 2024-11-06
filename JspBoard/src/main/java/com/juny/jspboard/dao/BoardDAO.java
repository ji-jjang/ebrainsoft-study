package com.juny.jspboard.dao;

import com.juny.jspboard.dto.ReqBoard;
import com.juny.jspboard.dto.ResBoardDetail;
import com.juny.jspboard.dto.ResBoardViewList;
import java.util.List;
import java.util.Map;

public interface BoardDAO {
  ResBoardViewList createBoard(ReqBoard req);
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
}

