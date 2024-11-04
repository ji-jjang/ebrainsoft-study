package com.juny.jspboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionTest {
  static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
  static final String USER = "ebsoft";
  static final String PASS = "ebsoft";

  public Connection getConnection() throws Exception{

    Connection conn = null;
    Statement stmt = null;

    try {
      System.out.println(DB_URL);
      System.out.println(USER);
      System.out.println(PASS);
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

    } catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }

    return conn;
  }
}
