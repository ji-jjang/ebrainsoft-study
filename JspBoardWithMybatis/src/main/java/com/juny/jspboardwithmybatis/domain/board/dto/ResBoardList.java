package com.juny.jspboardwithmybatis.domain.board.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResBoardList {

  private final List<ResBoardViewList> boards;
  private final ResSearchCondition searchCondition;
  private final ResPageInfo pageInfo;
}
