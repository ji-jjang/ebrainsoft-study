package com.juny.board.domain.board.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ResBoardList {

  private final List<ResBoardViewList> boards;
  private final ResSearchCondition searchCondition;
  private final ResPageInfo pageInfo;
}
