package com.juny.board.domain.board.mapper;

import com.juny.board.domain.board.dto.ResBoardImage;
import com.juny.board.domain.board.entity.BoardImage;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardImageMapper {

  ResBoardImage toResBoardImage(BoardImage boardImage);

  List<ResBoardImage> toResBoardImageList(List<BoardImage> boardImages);
}
