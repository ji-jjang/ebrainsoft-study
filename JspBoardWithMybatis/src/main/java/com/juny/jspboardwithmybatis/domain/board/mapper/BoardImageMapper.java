package com.juny.jspboardwithmybatis.domain.board.mapper;

import com.juny.jspboardwithmybatis.domain.board.entity.BoardImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardImageMapper {

  void saveBoardImage(BoardImage boardImage);

  void deleteBoardImageById(Long id);
}
