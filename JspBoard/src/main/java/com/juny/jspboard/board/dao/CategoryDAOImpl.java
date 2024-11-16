package com.juny.jspboard.board.dao;

import com.juny.jspboard.global.constant.Constants;
import com.juny.jspboard.global.constant.ErrorMessage;
import com.juny.jspboard.utility.DriverManagerUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

  @Override
  public List<String> getCategories() {

    List<String> categories = new ArrayList<>();
    String sql =
        """
    SELECT DISTINCT
        name
    FROM
        categories
    """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {

      while (rs.next()) {
        categories.add(rs.getString(Constants.NAME_COLUMN));
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return categories;
  }

  @Override
  public String getCategoryNameByCategoryId(Connection conn, Long categoryId) {

    String sql =
        """
    SELECT
        name
    FROM
        categories
    WHERE
        id = ?
    """;

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, categoryId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString(Constants.NAME_COLUMN);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException(ErrorMessage.SQL_ERROR_MSG + sql);
  }

  @Override
  public String getCategoryNameByCategoryId(Long categoryId) {

    String sql =
        """
    SELECT
        name
    FROM
        categories
    WHERE
        id = ?
    """;

    try (Connection conn = DriverManagerUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setLong(1, categoryId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString(Constants.NAME_COLUMN);
        }
      }
    } catch (SQLException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    throw new RuntimeException(ErrorMessage.GET_CATEGORY_NAME_FAILED_MSG + categoryId);
  }
}
