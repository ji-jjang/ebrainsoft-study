package com.juny.board.domain.board.entity.vo;

import com.juny.board.domain.board.entity.Board;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListVO {

  private List<Board> boards;
  private SearchConditionVO searchCondition;
  private PageInfoVO pageInfo;
}
