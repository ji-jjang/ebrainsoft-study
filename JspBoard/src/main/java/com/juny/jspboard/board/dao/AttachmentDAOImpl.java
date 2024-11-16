package com.juny.jspboard.board.dao;

import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import com.juny.jspboard.utility.DriverManagerUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAOImpl implements AttachmentDAO {

  @Override
  public boolean existsAttachment(Long boardId) {
    String sql = "SELECT COUNT(*) > 0 FROM attachments WHERE board_id = ?";
    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getBoolean(1);
        }
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException(ErrorMessage.SQL_ERROR_MSG + sql);
  }

  @Override
  public List<Attachment> getAttachmentsByBoardId(Connection conn, Long boardId) {

    String sql =
        """
      SELECT
          id, logical_name, logical_path, stored_name, stored_path, extension, size
      FROM
          attachments
      WHERE
          board_id = ?
      """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<Attachment> attachments = new ArrayList<>();
        while (rs.next()) {
          attachments.add(
              new Attachment(
                  rs.getLong(Constants.ID_COLUMN),
                  rs.getString(Constants.LOGICAL_NAME_COLUMN),
                  rs.getString(Constants.LOGICAL_PATH_COLUMN),
                  rs.getString(Constants.STORED_NAME_COLUMN),
                  rs.getString(Constants.STORED_PATH_COLUMN),
                  rs.getString(Constants.EXTENSION_COLUMN),
                  rs.getLong(Constants.SIZE_COLUMN),
                  boardId));
        }
        return attachments;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Attachment> getAttachmentsByBoardId(Long boardId) {

    String sql =
        """
      SELECT
          id, logical_name, logical_path, stored_name, stored_path, extension, size
      FROM
          attachments
      WHERE
          board_id = ?
      """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<Attachment> attachments = new ArrayList<>();
        while (rs.next()) {
          attachments.add(
              new Attachment(
                  rs.getLong(Constants.ID_COLUMN),
                  rs.getString(Constants.LOGICAL_NAME_COLUMN),
                  rs.getString(Constants.LOGICAL_PATH_COLUMN),
                  rs.getString(Constants.STORED_NAME_COLUMN),
                  rs.getString(Constants.STORED_PATH_COLUMN),
                  rs.getString(Constants.EXTENSION_COLUMN),
                  rs.getLong(Constants.SIZE_COLUMN),
                  boardId));
        }
        return attachments;
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAttachmentById(Connection conn, Long attachmentId) {
    String sql =
        """
    DELETE FROM
        attachments
    WHERE
        id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, attachmentId);
      int rows = pstmt.executeUpdate();
      if (rows == 0) {
        throw new SQLException(ErrorMessage.ROW_NOT_CHANGED_MSG + sql);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void saveAttachment(Connection conn, Attachment attachment) {

    String sql =
        """
    INSERT INTO
        attachments (logical_name, logical_path, stored_name, stored_path, extension, size, board_id)
    VALUES
        (?, ?, ?, ?, ?, ?, ?)
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, attachment.getLogicalName());
      pstmt.setString(2, attachment.getLogicalPath());
      pstmt.setString(3, attachment.getStoredName());
      pstmt.setString(4, attachment.getStoredPath());
      pstmt.setString(5, attachment.getExtension());
      pstmt.setLong(6, attachment.getSize());
      pstmt.setLong(7, attachment.getBoardId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
