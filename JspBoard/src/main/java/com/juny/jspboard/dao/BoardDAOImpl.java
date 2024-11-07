package com.juny.jspboard.dao;

import com.juny.jspboard.constant.ErrorMessage;
import com.juny.jspboard.dto.ReqBoardUpdate;
import com.juny.jspboard.dto.ResAttachment;
import com.juny.jspboard.dto.ResBoardDetail;
import com.juny.jspboard.dto.ResBoardImage;
import com.juny.jspboard.dto.ResBoardViewList;
import com.juny.jspboard.dto.ResComment;
import com.juny.jspboard.entity.Attachment;
import com.juny.jspboard.entity.Board;
import com.juny.jspboard.entity.BoardImage;
import com.juny.jspboard.utility.DriverManagerUtils;
import com.juny.jspboard.utility.TimeFormatterUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BoardDAOImpl implements BoardDAO {

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
      + "ORDER BY b.created_at DESC "
      + "LIMIT ? OFFSET ?";

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
  public ResBoardDetail getBoardDetail(Long boardId) {

    ResBoardDetail board = null;

    String sql =
      "SELECT b.id, b.title, b.created_by, b.created_at, b.updated_at, b.view_count, b.content, c.name AS category_name " +
        "FROM boards b " +
        "LEFT JOIN categories c ON b.category_id = c.id " +
        "WHERE b.id = ?";

    try (Connection conn = DriverManagerUtils.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setLong(1, boardId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {

        Long id = rs.getLong("id");
        String title = rs.getString("title");
        String createdBy = rs.getString("created_by");
        String createdAt = TimeFormatterUtils.datetimeToString(rs.getTimestamp("created_at").toLocalDateTime());
        String updatedAt = "-";
        if (rs.getTimestamp("updated_at") != null) {
          updatedAt = TimeFormatterUtils.datetimeToString(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        int viewCount = rs.getInt("view_count");
        String content = rs.getString("content");
        String categoryName = rs.getString("category_name");

        List<ResBoardImage> boardImages = getBoardImages(conn, boardId);
        List<ResAttachment> attachments = getAttachments(conn, boardId);
        List<ResComment> comments = getComments(conn, boardId);

        board =
          new ResBoardDetail(
            id,
            title,
            createdBy,
            createdAt,
            updatedAt,
            viewCount,
            content,
            categoryName,
            boardImages,
            attachments,
            comments);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return board;
  }

  @Override
  public ResBoardDetail updateBoard(ReqBoardUpdate reqBoardUpdate) {
    Connection conn = null;
    PreparedStatement boardPstmt = null;
    PreparedStatement imagePstmt = null;
    PreparedStatement attachmentPstmt = null;

    try {
      conn = DriverManagerUtils.getConnection();
      conn.setAutoCommit(false);

      if (!Objects.isNull(reqBoardUpdate.deleteImages())){
        for (var deleteImage : reqBoardUpdate.deleteImages()) {
          String[] split = deleteImage.split(",");
          Long boardImageId = Long.parseLong(split[0]);
          String storedPath = split[1];
          String storedName = split[2];
          String extension = split[3];
          File file = new File(storedPath + storedName + extension);
          if (file.exists()) {
            file.delete();
          }
          deleteBoardImageById(conn, boardImageId);
        }
      }

      if (!Objects.isNull(reqBoardUpdate.deleteAttachments())){
        for (var deleteAttachment : reqBoardUpdate.deleteAttachments()) {
          String[] split = deleteAttachment.split(",");
          Long attachmentId = Long.parseLong(split[0]);
          String storedPath = split[1];
          String storedName = split[2];
          String extension = split[3];
          File file = new File(storedPath + storedName + extension);
          if (file.exists()) {
            file.delete();
          }
          deleteAttachmentById(conn, attachmentId);
        }
      }

      String boardSql = "UPDATE boards SET created_by = ?, title = ?, updated_at = ?, content = ? WHERE id = ?";
      boardPstmt = conn.prepareStatement(boardSql);
      boardPstmt.setString(1, reqBoardUpdate.createdBy());
      boardPstmt.setString(2, reqBoardUpdate.updatedTitle());
      boardPstmt.setString(3, reqBoardUpdate.updatedAt());
      boardPstmt.setString(4, reqBoardUpdate.content());
      boardPstmt.setLong(5, reqBoardUpdate.boardId());
      int row = boardPstmt.executeUpdate();
      if (row == 0) {
        throw new SQLException(ErrorMessage.ROW_NOT_CHANGED_MSG + boardSql);
      }


      String imageSql = "INSERT INTO board_images (board_id, stored_path, stored_name, extension) VALUES (?, ?, ?, ?)";
      imagePstmt = conn.prepareStatement(imageSql);
      for (var image : reqBoardUpdate.boardImages()) {
        imagePstmt.setLong(1, reqBoardUpdate.boardId());
        imagePstmt.setString(2, image.getStoredPath());
        imagePstmt.setString(3, image.getStoredName());
        imagePstmt.setString(4, image.getExtension());
        row = imagePstmt.executeUpdate();
        if (row == 0) {
          throw new SQLException(ErrorMessage.ROW_NOT_CHANGED_MSG + imageSql);
        }
      }

      String attachmentSql = "INSERT INTO attachments (board_id, logical_name, logical_path, stored_name, stored_path, extension, size) VALUES (?, ?, ?, ?, ?, ?, ?)";
      attachmentPstmt = conn.prepareStatement(attachmentSql);
      for (var attachment : reqBoardUpdate.attachments()) {
        attachmentPstmt.setLong(1, reqBoardUpdate.boardId());
        attachmentPstmt.setString(2, attachment.getLogicalName());
        attachmentPstmt.setString(3, "");
        attachmentPstmt.setString(4, attachment.getStoredName());
        attachmentPstmt.setString(5, attachment.getStoredPath());
        attachmentPstmt.setString(6, attachment.getExtension());
        attachmentPstmt.setLong(7, attachment.getSize());
        row = attachmentPstmt.executeUpdate();
        if (row == 0) {
          throw new SQLException(ErrorMessage.ROW_NOT_CHANGED_MSG + attachmentSql);
        }
      }

      conn.commit();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  private void deleteBoardImageById(Connection conn, Long boardImageId) {

    String sql = "DELETE FROM board_images WHERE id = ?";

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

  private void deleteAttachmentById(Connection conn, Long attachmentId) {

    String sql = "DELETE FROM attachments WHERE id = ?";

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

  private List<ResAttachment> getAttachments(Connection conn, Long boardId) {

    List<ResAttachment> attachments = new ArrayList<>();

    String sql = "SELECT id, logical_name, stored_name, stored_path, extension FROM attachments WHERE board_id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        attachments.add(new ResAttachment(rs.getLong("id"), rs.getString("logical_name"), rs.getString("stored_name"), rs.getString("stored_path"), rs.getString("extension")));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return attachments;
  }

  private List<ResComment> getComments(Connection conn, Long boardId) {

    List<ResComment> comments = new ArrayList<>();

    String sql = "SELECT content, created_at, created_by FROM comments WHERE board_id = ? ORDER BY created_at ASC";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        comments.add(new ResComment(rs.getString("content"), rs.getString("created_at"), rs.getString("created_by")));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return comments;
  }

  private List<ResBoardImage> getBoardImages(Connection conn, Long boardId) {

    List<ResBoardImage> images = new ArrayList<>();
    String sql = "SELECT id, stored_path, stored_name, extension FROM board_images WHERE board_id = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        images.add(new ResBoardImage(rs.getLong("id"), rs.getString("stored_path"), rs.getString("stored_name"), rs.getString("extension")));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return images;
  }

  @Override
  public void deleteBoard(Long boardId) {

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
    sql.append(" ORDER BY b.created_at DESC ");
    sql.append(" LIMIT ? OFFSET ?");

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

  @Override
  public void increaseViewCount(Long boardId) {

    String sql = "UPDATE boards SET view_count = view_count + 1 WHERE id = ?";

    try (Connection conn = DriverManagerUtils.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void createComment(Long boardId, String name, String password, String content) {

    String sql = "INSERT INTO comments (board_id, created_by, password, content, created_at) VALUES (?, ?, ?, ?, NOW())";

    try (Connection conn = DriverManagerUtils.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, boardId);
      pstmt.setString(2, name);
      pstmt.setString(3, password);
      pstmt.setString(4, content);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Long getCategoryIdByName(String category) {

    String sql = "SELECT id FROM categories WHERE name = ?";

    try (Connection conn = DriverManagerUtils.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, category);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        return rs.getLong("id");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public Long createBoard(String category, Board board, List<BoardImage> images,
    List<Attachment> attachments) {

    Connection conn = null;
    PreparedStatement boardPstmt = null;
    PreparedStatement imagePstmt = null;
    PreparedStatement attachmentPstmt = null;

    Long boardId = null;

    try {
      conn = DriverManagerUtils.getConnection();
      conn.setAutoCommit(false);

      String boardSql = "INSERT INTO boards (title, content, password, view_count, created_at, updated_at, created_by, category_id) VALUES (?, ?, ?, ?, NOW(), NULL, ?, ?)";
      boardPstmt = conn.prepareStatement(boardSql, Statement.RETURN_GENERATED_KEYS);
      boardPstmt.setString(1, board.getTitle());
      boardPstmt.setString(2, board.getContent());
      boardPstmt.setString(3, board.getPassword());
      boardPstmt.setInt(4, board.getViewCount());
      boardPstmt.setString(5, board.getCreatedBy());
      boardPstmt.setLong(6, board.getCategoryId());
      boardPstmt.executeUpdate();

      ResultSet rs = boardPstmt.getGeneratedKeys();
      if (rs.next()) {
        boardId = rs.getLong(1);
      }
      rs.close();

      String imageSql = "INSERT INTO board_images (board_id, stored_path, stored_name, extension) VALUES (?, ?, ?, ?)";
      imagePstmt = conn.prepareStatement(imageSql);
      for (var image : images) {
        imagePstmt.setLong(1, boardId);
        imagePstmt.setString(2, image.getStoredPath());
        imagePstmt.setString(3, image.getStoredName());
        imagePstmt.setString(4, image.getExtension());
        imagePstmt.executeUpdate();
      }

      String attachmentSql = "INSERT INTO attachments (board_id, logical_name, logical_path, stored_name, stored_path, extension, size) VALUES (?, ?, ?, ?, ?, ?, ?)";
      attachmentPstmt = conn.prepareStatement(attachmentSql);
      for (Attachment attachment : attachments) {
        attachmentPstmt.setLong(1, boardId);
        attachmentPstmt.setString(2, attachment.getLogicalName());
        attachmentPstmt.setString(3, "");
        attachmentPstmt.setString(4, attachment.getStoredName());
        attachmentPstmt.setString(5, attachment.getStoredPath());
        attachmentPstmt.setString(6, attachment.getExtension());
        attachmentPstmt.setLong(7, attachment.getSize());
        attachmentPstmt.executeUpdate();
      }

      conn.commit();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return boardId;
  }

  private String handleLongTitle(String title) {

    if (!Objects.isNull(title) && title.length() > 80) {
      return title.substring(0, 80) + "...";
    }
    return title;
  }
}
