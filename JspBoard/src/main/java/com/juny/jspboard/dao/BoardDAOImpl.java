package com.juny.jspboard.dao;

import com.juny.jspboard.dto.ReqBoard;
import com.juny.jspboard.dto.ResBoard;
import java.util.List;

public class BoardDAOImpl implements BoardDAO {

  @Override
  public ResBoard createBoard(ReqBoard req) {
    return null;
  }

  @Override
  public List<ResBoard> getBoardList() {
    return List.of();
  }

  @Override
  public ResBoard getBoardDetail(int boardId) {
    return null;
  }

  @Override
  public ResBoard updateBoard(int boardId) {
    return null;
  }

  @Override
  public void deleteBoard(int boardId) {

  }
}
