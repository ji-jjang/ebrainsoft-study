package com.juny.jspboardwithmybatis.domain.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryMapperUtils {

  private static final Map<Long, String> CATEGORY_MAP = new HashMap<>();

  static {
    CATEGORY_MAP.put(1L, "Java");
    CATEGORY_MAP.put(2L, "Javascript");
    CATEGORY_MAP.put(3L, "Database");
  }

  public static String getCategoryNameById(long id) {

    return CATEGORY_MAP.get(id);
  }

  public static Map<Long, String> getCategoryMap() {

    return Collections.unmodifiableMap(CATEGORY_MAP);
  }

  public static List<String> getAllCategoryName() {

    return new ArrayList<>(CATEGORY_MAP.values());
  }
}
