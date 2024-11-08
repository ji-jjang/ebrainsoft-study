package com.juny.jspboard.board.dao;

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
}
