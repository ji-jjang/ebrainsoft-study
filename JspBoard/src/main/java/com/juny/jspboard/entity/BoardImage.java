package com.juny.jspboard.entity;

public class BoardImage {

  private Long id;
  private String storedName;
  private String storedPath;
  private String extension;
  private Long boardId;

  public BoardImage(String storedName, String storedPath, String extension) {
    this.storedName = storedName;
    this.storedPath = storedPath;
    this.extension = extension;
  }

  public Long getId() {
    return id;
  }

  public String getStoredName() {
    return storedName;
  }

  public String getStoredPath() {
    return storedPath;
  }

  public String getExtension() {
    return extension;
  }

  public Long getBoardId() {
    return boardId;
  }
}
