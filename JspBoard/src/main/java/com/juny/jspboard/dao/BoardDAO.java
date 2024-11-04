package com.juny.jspboard.dao;

import com.juny.jspboard.dto.ReqBoard;
import com.juny.jspboard.dto.ResBoard;
import java.util.List;

public interface BoardDAO {
  ResBoard createBoard(ReqBoard req);
  List<ResBoard> getBoardList();
  ResBoard getBoardDetail(int boardId);
  ResBoard updateBoard(int boardId);
  void deleteBoard(int boardId);
}

