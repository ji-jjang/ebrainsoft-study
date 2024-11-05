package com.juny.jspboard.dao;

import com.juny.jspboard.dto.ReqBoard;
import com.juny.jspboard.dto.ResBoardViewList;
import com.juny.jspboard.utility.DriverManagerUtils;
import com.juny.jspboard.utility.TimeFormatterUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BoardDAOImpl implements BoardDAO {

  @Override
  public ResBoardViewList createBoard(ReqBoard req) {
    return null;
  }

  @Override
  public List<ResBoardViewList> getBoardList(int page) {

    List<ResBoardViewList> boards = new ArrayList<>();

    int limit = 10;
    int offset = (page - 1) * limit;

    String sql = "SELECT c.name as category_name, "
      + "b.id, "
      + "EXISTS (SELECT 1 FROM attachments a WHERE a.board_id = b.id) AS has_attachment, "
      + "b.title, b.created_by, b.view_count, b.created_at, b.updated_at "
      + "FROM boards b "
      + "LEFT JOIN categories c ON b.category_id = c.id "
      + "LEFT JOIN attachments a ON b.id = a.board_id "
      + "GROUP BY b.id, c.name, b.title, b.created_by, b.view_count, b.created_at, b.updated_at "
      + "LIMIT ? OFFSET ?";

    System.out.println("sql: " + sql);
    try (Connection conn = DriverManagerUtils.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, limit);
      pstmt.setInt(2, offset);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {

        String updatedAt = "-";
        if (rs.getTimestamp("updated_at") != null) {
          updatedAt = TimeFormatterUtils.datetimeToString(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        ResBoardViewList board =
          new ResBoardViewList(
            rs.getString("category_name"),
            rs.getLong("id"),
            rs.getBoolean("has_attachment"),
            handleLongTitle(rs.getString("title")),
            rs.getString("created_by"),
            rs.getInt("view_count"),
            TimeFormatterUtils.datetimeToString(rs.getTimestamp("created_at").toLocalDateTime()),
            updatedAt);

        boards.add(board);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    return boards;
  }

  @Override
  public ResBoardViewList getBoardDetail(int boardId) {
    return null;
  }

  @Override
  public ResBoardViewList updateBoard(int boardId) {
    return null;
  }

  @Override
  public void deleteBoard(int boardId) {

  }

  @Override
  public List<ResBoardViewList> getBoardSearchList(int page, Map<String, String> searchConditions) {

    List<ResBoardViewList> boards = new ArrayList<>();

    StringBuilder sql = new StringBuilder(
      "SELECT b.id, b.title, b.created_by, b.view_count, b.created_at, b.updated_at "
        + "FROM boards b "
        + "LEFT JOIN categories c ON b.category_id = c.id "
    );

    int limit = 10;
    int offset = (page - 1) * limit;

    boolean hasWhere = false;

    if (searchConditions.containsKey("startDate") && searchConditions.containsKey("endDate")) {
      sql.append("WHERE b.created_at BETWEEN ? AND ? ");
      hasWhere = true;
    }

    if (searchConditions.containsKey("category")) {
      String connector = hasWhere ? "AND" : "WHERE";
      sql.append(connector).append(" c.name = ? ");
    }

    if (searchConditions.containsKey("keyword")) {
      String connector  = hasWhere ? "AND" : "WHERE";
      sql.append(connector)
        .append(" (b.title LIKE ? OR b.created_by LIKE ? OR b.content LIKE ?) ");
    }
    sql.append(" LIMIT ? OFFSET ?");

    System.out.println("sql: " + sql);
    try (Connection conn = DriverManagerUtils.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

      int index = 0;

      if (searchConditions.containsKey("startDate") && searchConditions.containsKey("endDate")) {
        pstmt.setString(++index, searchConditions.get("startDate") + " 00:00:00");
        pstmt.setString(++index, searchConditions.get("endDate") + " 23:59:59");
      }

      if (searchConditions.containsKey("category")) {
        pstmt.setString(++index, searchConditions.get("category"));
      }

      if (searchConditions.containsKey("keyword")) {
        String keyword = "%" + searchConditions.get("keyword") + "%";
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
      }

      pstmt.setInt(++index, limit);
      pstmt.setInt(++index, offset);

      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {

        long boardId = rs.getLong("id");
        String categoryName = getCategoryName(conn, boardId);
        boolean hasAttachment = checkHasAttachment(conn, boardId);

        String updatedAt = "-";
        if (rs.getTimestamp("updated_at") != null) {
          updatedAt = TimeFormatterUtils.datetimeToString(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        ResBoardViewList board =
          new ResBoardViewList(
            categoryName,
            boardId,
            hasAttachment,
            handleLongTitle(rs.getString("title")),
            rs.getString("created_by"),
            rs.getInt("view_count"),
            TimeFormatterUtils.datetimeToString(rs.getTimestamp("created_at").toLocalDateTime()),
            updatedAt);

        boards.add(board);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    return boards;
  }

  private boolean checkHasAttachment(Connection conn, long boardId) {

    String attachmentSql = "SELECT COUNT(*) > 0 FROM attachments WHERE board_id = ?";
    try (PreparedStatement attachmentStmt = conn.prepareStatement(attachmentSql)) {
      attachmentStmt.setLong(1, boardId);

      ResultSet rs = attachmentStmt.executeQuery();
      if (rs.next()) {
        return rs.getBoolean(1);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  private String getCategoryName(Connection conn, long boardId) {

    String sql = "SELECT c.name FROM categories c "
      + "JOIN boards b ON b.category_id = c.id "
      + "WHERE b.id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getString("name");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public List<String> getCategories() {

    List<String> categories = new ArrayList<>();
    String sql = "SELECT DISTINCT name FROM categories";
    try (Connection conn = DriverManagerUtils.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery()) {

      while (rs.next()) {
        categories.add(rs.getString("name"));
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return categories;
  }

  @Override
  public int getTotals() {

    String sql = "SELECT COUNT(*) FROM boards";

    try (Connection conn = DriverManagerUtils.getConnection();

      PreparedStatement pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery()) {

      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return 0;
  }

  @Override
  public int getTotalsWithSearchConditions(Map<String, String> searchConditions) {

    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM boards b ");
    sql.append("LEFT JOIN categories c ON b.category_id = c.id ");

    boolean hasWhere = false;

    if (searchConditions.containsKey("startDate") && searchConditions.containsKey("endDate")) {
      sql.append("WHERE b.created_at BETWEEN ? AND ? ");
      hasWhere = true;
    }

    if (searchConditions.containsKey("category")) {
      String connector = hasWhere ? "AND " : "WHERE ";
      sql.append(connector).append("c.name = ? ");
      hasWhere = true;
    }

    if (searchConditions.containsKey("keyword")) {
      String connector = hasWhere ? "AND " : "WHERE ";
      sql.append(connector)
        .append("(b.title LIKE ? OR b.created_by LIKE ? OR b.content LIKE ?) ");
    }

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

      int index = 0;
      if (searchConditions.containsKey("startDate") && searchConditions.containsKey("endDate")) {
        pstmt.setString(++index, searchConditions.get("startDate") + " 00:00:00");
        pstmt.setString(++index, searchConditions.get("endDate") + " 23:59:59");
      }

      if (searchConditions.containsKey("category")) {
        pstmt.setString(++index, searchConditions.get("category"));
      }

      if (searchConditions.containsKey("keyword")) {
        String keyword = "%" + searchConditions.get("keyword") + "%";
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
        pstmt.setString(++index, keyword);
      }
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return 0;
  }

  private String handleLongTitle(String title) {

    if (!Objects.isNull(title) && title.length() > 80) {
      return title.substring(0, 80) + "...";
    }
    return title;
  }

}
