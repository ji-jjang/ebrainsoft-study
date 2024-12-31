package com.juny.finalboard.domain.post.common;

public interface FileDetails {
  Long getId();
  String getLogicalName();
  String getStoredPath();
  String getStoredName();
  String getExtension();
  Long getSize();
}
