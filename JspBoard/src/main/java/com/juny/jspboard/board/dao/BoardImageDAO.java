package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.entity.BoardImage;
import java.sql.Connection;
import java.util.List;

public interface BoardImageDAO {

  List<BoardImage> getBoardImages(Connection conn, Long boardId);

  List<BoardImage> getBoardImages(Long boardId);

  void deleteBoardImageById(Connection conn, Long boardImageId);

  void saveBoardImage(Connection conn, BoardImage boardImage);
}
