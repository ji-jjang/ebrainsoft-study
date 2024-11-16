package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.entity.Comment;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.utility.DriverManagerUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAOImpl implements CommentDAO {

  @Override
  public List<Comment> getComments(Connection conn, Long boardId) {

    String sql =
        """
      SELECT
          id, password, content, created_at, created_by
      FROM
          comments
      WHERE
          board_id = ?
      ORDER BY
          created_at
      DESC
      """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<Comment> comments = new ArrayList<>();
        while (rs.next()) {
          comments.add(
              new Comment(
                  rs.getLong(Constants.ID_COLUMN),
                  rs.getString(Constants.PASSWORD_COLUMN),
                  rs.getString(Constants.CONTENT_COLUMN),
                  rs.getTimestamp(Constants.CREATED_AT_COLUMN).toLocalDateTime(),
                  rs.getString(Constants.CREATED_BY_COLUMN),
                  boardId));
        }
        return comments;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Comment> getComments(Long boardId) {

    String sql =
        """
      SELECT
          id, password, content, created_at, created_by
      FROM
          comments
      WHERE
          board_id = ?
      ORDER BY
          created_at
      DESC
      """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<Comment> comments = new ArrayList<>();
        while (rs.next()) {
          comments.add(
              new Comment(
                  rs.getLong(Constants.ID_COLUMN),
                  rs.getString(Constants.PASSWORD_COLUMN),
                  rs.getString(Constants.CONTENT_COLUMN),
                  rs.getTimestamp(Constants.CREATED_AT_COLUMN).toLocalDateTime(),
                  rs.getString(Constants.CREATED_BY_COLUMN),
                  boardId));
        }
        return comments;
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void createComment(Comment comment) {

    String sql =
        """
    INSERT INTO
        comments (content, password, created_At, created_by, board_id)
    VALUES
        (?, ?, NOW(), ?, ?)
    """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, comment.getContent());
      pstmt.setString(2, comment.getPassword());
      pstmt.setString(3, comment.getCreatedBy());
      pstmt.setLong(4, comment.getBoardId());
      pstmt.executeUpdate();

    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
