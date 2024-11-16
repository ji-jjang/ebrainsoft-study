package com.juny.jspboard.board.dao;

import java.sql.Connection;
import java.util.List;

public interface CategoryDAO {
  List<String> getCategories();

  String getCategoryNameByCategoryId(Connection conn, Long categoryId);

  String getCategoryNameByCategoryId(Long categoryId);
}
