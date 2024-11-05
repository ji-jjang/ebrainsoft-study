package com.juny.jspboard.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// TODO 1.상수화. 2. .env 파일을 읽어서 설정할 수 있도록

/**
 * The type Driver manager utils.
 */
public class DriverManagerUtils {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
  private static final String USER = "ebsoft";
  private static final String PASS = "ebsoft";

  /**
   * DB 연결 얻는 메서드
   *
   * @return the connection
   * @throws ClassNotFoundException the class not found exception
   */
  public static Connection getConnection() throws ClassNotFoundException {

    Connection conn = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
    } catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
    return conn;
  }

  private DriverManagerUtils() {
  }
}
