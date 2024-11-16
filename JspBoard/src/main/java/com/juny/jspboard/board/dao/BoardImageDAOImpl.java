package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.entity.BoardImage;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import com.juny.jspboard.utility.DriverManagerUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardImageDAOImpl implements BoardImageDAO {

  @Override
  public List<BoardImage> getBoardImages(Connection conn, Long boardId) {

    String sql =
        """
      SELECT
          id, stored_name, stored_path, extension
      FROM
          board_images
      WHERE
          board_id = ?
      """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<BoardImage> images = new ArrayList<>();
        while (rs.next()) {
          images.add(
              new BoardImage(
                  rs.getLong(Constants.ID_COLUMN),
                  rs.getString(Constants.STORED_NAME_COLUMN),
                  rs.getString(Constants.STORED_PATH_COLUMN),
                  rs.getString(Constants.EXTENSION_COLUMN),
                  boardId));
        }
        return images;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<BoardImage> getBoardImages(Long boardId) {

    String sql =
        """
      SELECT
          id, stored_name, stored_path, extension
      FROM
          board_images
      WHERE
          board_id = ?
      """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<BoardImage> images = new ArrayList<>();
        while (rs.next()) {
          images.add(
              new BoardImage(
                  rs.getLong(Constants.ID_COLUMN),
                  rs.getString(Constants.STORED_NAME_COLUMN),
                  rs.getString(Constants.STORED_PATH_COLUMN),
                  rs.getString(Constants.EXTENSION_COLUMN),
                  boardId));
        }
        return images;
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteBoardImageById(Connection conn, Long boardImageId) {

    String sql =
        """
    DELETE FROM
        board_images
    WHERE
        id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardImageId);
      int rows = pstmt.executeUpdate();
      if (rows == 0) {
        throw new SQLException(ErrorMessage.ROW_NOT_CHANGED_MSG + sql);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void saveBoardImage(Connection conn, BoardImage image) {
    String sql =
        """
    INSERT INTO
      board_images (board_id, stored_name, stored_path, extension)
    VALUES (?, ?, ?, ?)
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, image.getBoardId());
      pstmt.setString(2, image.getStoredName());
      pstmt.setString(3, image.getStoredPath());
      pstmt.setString(4, image.getExtension());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
