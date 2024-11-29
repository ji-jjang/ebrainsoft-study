package com.juny.board.domain.board.repository;

import com.juny.board.domain.board.entity.BoardImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardImageRepository {

  void saveBoardImage(BoardImage boardImage);

  void deleteBoardImageById(Long id);
}
