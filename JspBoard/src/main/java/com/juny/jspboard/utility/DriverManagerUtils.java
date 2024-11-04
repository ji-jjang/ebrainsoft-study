package com.juny.jspboard.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerUtils {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
  private static final String USER = "ebsoft";
  private static final String PASS = "ebsoft";

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
