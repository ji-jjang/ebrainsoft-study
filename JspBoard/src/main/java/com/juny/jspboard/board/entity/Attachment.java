package com.juny.jspboard.board.entity;

public class Attachment {

  private Long id;
  private String logicalName;
  private String logicalPath;
  private String storedName;
  private String storedPath;
  private String extension;
  private Long size;
  private Long boardId;

  public Attachment(
      Long id,
      String logicalName,
      String logicalPath,
      String storedName,
      String storedPath,
      String extension,
      Long size,
      Long boardId) {
    this.id = id;
    this.logicalName = logicalName;
    this.logicalPath = logicalPath;
    this.storedName = storedName;
    this.storedPath = storedPath;
    this.extension = extension;
    this.size = size;
    this.boardId = boardId;
  }

  public Attachment(
      String logicalName,
      String logicalPath,
      String storedName,
      String storedPath,
      String extension,
      Long size,
      Long boardId) {
    this.logicalName = logicalName;
    this.logicalPath = logicalPath;
    this.storedName = storedName;
    this.storedPath = storedPath;
    this.extension = extension;
    this.size = size;
    this.boardId = boardId;
  }

  public Long getId() {
    return id;
  }

  public String getLogicalName() {
    return logicalName;
  }

  public String getLogicalPath() {
    return logicalPath;
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

  public Long getSize() {
    return size;
  }

  public Long getBoardId() {
    return boardId;
  }
}
