package com.juny.board.domain.board.entity;

import java.util.Optional;

public interface FileInfo {

  Optional<String> logicalName();

  String storedName();

  String storedPath();

  Optional<String> extension();

  Optional<Integer> size();
}
