package com.juny.board.domain.board.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ResCategoryNames {

  List<String> categoryNames;
}
