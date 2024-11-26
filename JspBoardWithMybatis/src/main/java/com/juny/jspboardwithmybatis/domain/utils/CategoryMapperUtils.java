package com.juny.jspboardwithmybatis.domain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 간단한 카테고리 목록, DB 조회보다는 정적 메모리에서 가져오기 위함 */
public class CategoryMapperUtils {

  private static final Map<Long, String> CATEGORY_MAP = new HashMap<>();

  static {
    CATEGORY_MAP.put(1L, "Java");
    CATEGORY_MAP.put(2L, "Javascript");
    CATEGORY_MAP.put(3L, "Database");
  }

  /**
   *
   *
   * <h1>카테고리 이름으로 아이디 찾기</h1>
   *
   * @param categoryName
   * @return categoryId
   */
  public static Long getCategoryIdByName(String categoryName) {
    for (var entry : CATEGORY_MAP.entrySet()) {
      if (entry.getValue().equals(categoryName)) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   *
   *
   * <h1>카테고리 모든 목록 찾기(게시판 생성 폼)</h1>
   *
   * @return all categories name
   */
  public static List<String> getAllCategoryName() {

    return new ArrayList<>(CATEGORY_MAP.values());
  }
}
