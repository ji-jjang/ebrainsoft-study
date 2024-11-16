package com.juny.jspboard.board.dao;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import com.juny.jspboard.board.entity.Attachment;
import com.juny.jspboard.board.entity.Board;
import com.juny.jspboard.board.entity.BoardImage;
import com.juny.jspboard.utility.DriverManagerUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BoardDAOImpl implements BoardDAO {

  @Override
  public Long saveBoard(Connection conn, Board board) {

    String sql =
        """
    INSERT INTO
        boards (title, content, password, view_count, created_at, created_by, updated_at, category_id)
    VALUES
        (?, ?, ?, ?, NOW(), ?, NULL, ?)
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      pstmt.setString(1, board.getTitle());
      pstmt.setString(2, board.getContent());
      pstmt.setString(3, board.getPassword());
      pstmt.setInt(4, board.getViewCount());
      pstmt.setString(5, board.getCreatedBy());
      pstmt.setLong(6, board.getCategoryId());
      pstmt.executeUpdate();

      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          return rs.getLong(1);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException(ErrorMessage.SQL_ERROR_MSG + sql);
  }

  @Override
  public void saveBoardImage(Connection conn, BoardImage boardImage) {

    String sql =
        """
    INSERT INTO
        board_images (stored_name, stored_path, extension, board_id)
    VALUES
        (?, ?, ?, ?)
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, boardImage.getStoredName());
      pstmt.setString(2, boardImage.getStoredPath());
      pstmt.setString(3, boardImage.getExtension());
      pstmt.setLong(4, boardImage.getBoardId());
      pstmt.executeUpdate();
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

  @Override
  public List<String> findImagePathsByBoardId(Connection conn, Long boardId) {

    String sql =
        """
    SELECT
        stored_path
    FROM
        board_images
    WHERE
        board_id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<String> paths = new ArrayList<>();
        while (rs.next()) {
          paths.add(rs.getString(Constants.STORED_PATH_COLUMN));
        }
        return paths;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<String> findAttachmentPathsByBoardId(Connection conn, Long boardId) {

    String sql =
        """
    SELECT
        stored_path
    FROM
        attachments
    WHERE
        board_id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        List<String> paths = new ArrayList<>();
        while (rs.next()) {
          paths.add(rs.getString(Constants.STORED_PATH_COLUMN));
        }
        return paths;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteCommentsByBoardId(Connection conn, Long boardId) {

    String sql =
        """
    DELETE FROM
        comments
    WHERE
        board_id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAttachmentsByBoardId(Connection conn, Long boardId) {

    String sql =
        """
    DELETE FROM
        attachments
    WHERE
        board_id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteImagesByBoardId(Connection conn, Long boardId) {

    String sql =
        """
    DELETE FROM
        board_images
    WHERE
        board_id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteBoard(Connection conn, Long boardId) {

    String sql =
        """
    DELETE FROM
        boards
    WHERE
        id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void increaseViewCount(Connection conn, Long boardId) {

    String sql =
        """
    UPDATE
        boards
    SET
        view_count = view_count + 1
    WHERE
        id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Board getBoardDetail(Long boardId) {

    String sql =
        """
    SELECT
        id, title, content, password, view_count, created_at, created_by, updated_at, category_id
    FROM
        boards
    where
        id = ?
    """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return new Board(
              rs.getLong(Constants.ID_COLUMN),
              rs.getString(Constants.TITLE_COLUMN),
              rs.getString(Constants.CONTENT_COLUMN),
              rs.getString(Constants.PASSWORD_COLUMN),
              rs.getInt(Constants.VIEW_COUNT_COLUMN),
              rs.getTimestamp(Constants.CREATED_AT_COLUMN).toLocalDateTime(),
              rs.getString(Constants.CREATED_BY_COLUMN),
              rs.getTimestamp(Constants.UPDATED_AT_COLUMN) != null
                  ? rs.getTimestamp(Constants.UPDATED_AT_COLUMN).toLocalDateTime()
                  : null,
              rs.getLong(Constants.CATEGORY_ID_COLUMN));
        }
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException(ErrorMessage.SQL_ERROR_MSG + sql);
  }

  @Override
  public Board getBoardDetail(Connection conn, Long boardId) {

    String sql =
        """
      SELECT
          id, title, content, password, view_count, created_at, created_by, updated_at, category_id
      FROM
          boards
      where
          id = ?
      """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return new Board(
              rs.getLong(Constants.ID_COLUMN),
              rs.getString(Constants.TITLE_COLUMN),
              rs.getString(Constants.CONTENT_COLUMN),
              rs.getString(Constants.PASSWORD_COLUMN),
              rs.getInt(Constants.VIEW_COUNT_COLUMN),
              rs.getTimestamp(Constants.CREATED_AT_COLUMN).toLocalDateTime(),
              rs.getString(Constants.CREATED_BY_COLUMN),
              rs.getTimestamp(Constants.UPDATED_AT_COLUMN) != null
                  ? rs.getTimestamp(Constants.UPDATED_AT_COLUMN).toLocalDateTime()
                  : null,
              rs.getLong(Constants.CATEGORY_ID_COLUMN));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException(ErrorMessage.SQL_ERROR_MSG + sql);
  }

  @Override
  public List<Board> getBoardList(int page, Map<String, String> searchConditions) {

    List<Board> boards = new ArrayList<>();

    StringBuilder sql =
        new StringBuilder(
            """
        SELECT
          b.id, b.title, b.content, b.view_count, b.created_at, b.created_by, b.updated_at, b.category_id
        FROM
          boards b
        LEFT JOIN
          categories c
        ON
          b.category_id = c.id
        """);

    int limit = Constants.BOARD_LIST_PAGE_SIZE;
    int offset = (page - 1) * limit;

    boolean hasWhere = false;

    if (searchConditions.containsKey(Constants.START_DATE)
        && searchConditions.containsKey(Constants.END_DATE)) {
      sql.append("WHERE b.created_at BETWEEN ? AND ? ");
      hasWhere = true;
    }

    if (searchConditions.containsKey(Constants.CATEGORY)) {
      String connector = hasWhere ? "AND" : "WHERE";
      sql.append(connector).append(" c.name = ? ");
      hasWhere = true;
    }

    if (searchConditions.containsKey(Constants.KEYWORD)) {
      String connector = hasWhere ? "AND" : "WHERE";
      sql.append(connector).append(" (b.title LIKE ? OR b.created_by LIKE ? OR b.content LIKE ?) ");
    }
    sql.append(" ORDER BY b.created_at DESC ");
    sql.append(" LIMIT ? OFFSET ?");

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

      int index = 0;

      if (searchConditions.containsKey(Constants.START_DATE)
          && searchConditions.containsKey(Constants.END_DATE)) {
        pstmt.setString(
            ++index, searchConditions.get(Constants.START_DATE) + Constants.START_DATE_START_TIME);
        pstmt.setString(
            ++index, searchConditions.get(Constants.END_DATE) + Constants.END_DATE_END_TIME);
      }

      if (searchConditions.containsKey(Constants.CATEGORY)) {
        pstmt.setString(++index, searchConditions.get(Constants.CATEGORY));
      }

      if (searchConditions.containsKey(Constants.KEYWORD)) {
        String keyword =
            Constants.PERSENT_SIGN
                + searchConditions.get(Constants.KEYWORD)
                + Constants.PERSENT_SIGN;
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
      }

      pstmt.setInt(++index, limit);
      pstmt.setInt(++index, offset);

      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          boards.add(
              new Board(
                  rs.getLong(Constants.ID_COLUMN),
                  rs.getString(Constants.TITLE_COLUMN),
                  rs.getString(Constants.CONTENT_COLUMN),
                  null,
                  rs.getInt(Constants.VIEW_COUNT_COLUMN),
                  rs.getTimestamp(Constants.CREATED_AT_COLUMN).toLocalDateTime(),
                  rs.getString(Constants.CREATED_BY_COLUMN),
                  rs.getTimestamp(Constants.UPDATED_AT_COLUMN) != null
                      ? rs.getTimestamp(Constants.UPDATED_AT_COLUMN).toLocalDateTime()
                      : null,
                  rs.getLong(Constants.CATEGORY_ID_COLUMN)));
        }
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return boards;
  }

  @Override
  public int getTotals(Map<String, String> searchConditions) {

    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM boards b ");
    sql.append("LEFT JOIN categories c ON b.category_id = c.id ");

    boolean hasWhere = false;

    if (searchConditions.containsKey(Constants.START_DATE)
        && searchConditions.containsKey(Constants.END_DATE)) {
      sql.append("WHERE b.created_at BETWEEN ? AND ? ");
      hasWhere = true;
    }

    if (searchConditions.containsKey(Constants.CATEGORY)) {
      String connector = hasWhere ? "AND " : "WHERE ";
      sql.append(connector).append("c.name = ? ");
      hasWhere = true;
    }

    if (searchConditions.containsKey(Constants.KEYWORD)) {
      String connector = hasWhere ? "AND " : "WHERE ";
      sql.append(connector).append("(b.title LIKE ? OR b.created_by LIKE ? OR b.content LIKE ?) ");
    }

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

      int index = 0;
      if (searchConditions.containsKey(Constants.START_DATE)
          && searchConditions.containsKey(Constants.END_DATE)) {
        pstmt.setString(
            ++index, searchConditions.get(Constants.START_DATE) + Constants.START_DATE_START_TIME);
        pstmt.setString(
            ++index, searchConditions.get(Constants.END_DATE) + Constants.END_DATE_END_TIME);
      }

      if (searchConditions.containsKey(Constants.CATEGORY)) {
        pstmt.setString(++index, searchConditions.get(Constants.CATEGORY));
      }

      if (searchConditions.containsKey(Constants.KEYWORD)) {
        String keyword =
            Constants.PERSENT_SIGN
                + searchConditions.get(Constants.KEYWORD)
                + Constants.PERSENT_SIGN;
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
      }
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    return 0;
  }

  @Override
  public Long getCategoryIdByName(String category) {

    String sql = "SELECT id FROM categories WHERE name = ?";

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, category);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return rs.getLong(Constants.ID_COLUMN);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void updateBoard(Connection conn, Board board) {
    String sql =
        """
    UPDATE
        boards
    SET
        title = ?, content = ?, updated_at = ?
    WHERE
        id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, board.getTitle());
      pstmt.setString(2, board.getContent());
      pstmt.setTimestamp(3, Timestamp.valueOf(board.getUpdatedAt()));
      pstmt.setLong(4, board.getId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getStoredPassword(Long boardId) {

    String sql =
        """
    SELECT
        password
    FROM
        boards
    WHERE
        id = ?
    """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setLong(1, boardId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getString(Constants.PASSWORD_COLUMN);
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
