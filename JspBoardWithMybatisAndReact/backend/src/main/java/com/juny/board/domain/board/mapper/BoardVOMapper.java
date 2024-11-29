package com.juny.board.domain.board.mapper;

import com.juny.board.domain.board.dto.ResPageInfo;
import com.juny.board.domain.board.dto.ResSearchCondition;
import com.juny.board.domain.board.entity.vo.PageInfoVO;
import com.juny.board.domain.board.entity.vo.SearchConditionVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardVOMapper {

  ResSearchCondition toResSearchCondition(SearchConditionVO searchConditionVO);

  ResPageInfo toResPageInfo(PageInfoVO pageInfoVO);
}
