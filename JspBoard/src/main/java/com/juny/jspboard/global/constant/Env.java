package com.juny.jspboard.global.constant;

/**
 * 추후 Secret Key 적용할 수 있음
 *
 * <p>아래처럼 저장하게 되면, 배포된 서블릿 안에 저장되어 서버를 새로 실행될 때마다 파일이 초기화, 새롭게 배포해야 이미지가 불러짐 ServletContext context
 * = getServletContext(); this.uploadPath = context.getRealPath("/attachments/"); this.imagePath =
 * context.getRealPath("/images/"); - classpath 아닌 경로에 저장하고, 톰캣 서버 설정에서 해당 리소스 경로를 추가한다.
 *
 * <p>-> 해당 설정이 갑자기 안되어 이미지 파일을 읽는 서블릿 추가
 */
public final class Env {

  public static final String ATTACHMENT_PATH =
      "/Users/jijunhyuk/JunyProjects/ebrainsoft/attachments/";
  public static final String IMAGE_PATH = "/Users/jijunhyuk/JunyProjects/ebrainsoft/images/";
  public static final String DB_URL = "jdbc:mysql://localhost:3306/ebrainsoft_study";
  public static final String DB_USER = "ebsoft";
  public static final String DB_PASSWORD = "ebsoft";
  public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
  public static final String RESOURCE_PATH = "/resources";

  private Env() {}
}
