package com.juny.jspboard.utility;

import com.juny.jspboard.global.constant.Env;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerUtils {

  private DriverManagerUtils() {}

  /**
   * DB 연결 얻는 메서드
   *
   * @return the connection
   * @throws ClassNotFoundException the class not found exception
   */
  public static Connection getConnection() throws ClassNotFoundException {

    Connection conn = null;

    try {
      Class.forName(Env.DB_DRIVER);
      conn = DriverManager.getConnection(Env.DB_URL, Env.DB_USER, Env.DB_PASSWORD);
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
      System.out.println(ex.getSQLState());
      System.out.println(ex.getErrorCode());
    }
    return conn;
  }
}
