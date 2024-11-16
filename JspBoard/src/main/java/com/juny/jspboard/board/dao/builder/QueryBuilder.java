package com.juny.jspboard.board.dao.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QueryBuilder {

  private final StringBuilder sql;
  private final List<Object> parameters;

  private QueryBuilder(String baseQuery) {
    this.sql = new StringBuilder(baseQuery);
    this.parameters = new ArrayList<>();
  }

  public static QueryBuilder create(String baseQuery) {
    return new QueryBuilder(baseQuery);
  }

  public QueryBuilder addCondition(String condition, Object... params) {
    if (!Objects.isNull(condition)) {
      sql.append(parameters.isEmpty() ? " WHERE " : " AND ").append(condition);
      if (!Objects.isNull(params)) {
        parameters.addAll(Arrays.asList(params));
      }
    }
    return this;
  }

  public QueryBuilder orderBy(String orderBy) {
    if (!Objects.isNull(orderBy)) {
      sql.append(" ORDER BY ").append(orderBy);
    }
    return this;
  }

  public QueryBuilder limitOffset(int limit, int offset) {
    sql.append(" LIMIT ? OFFSET ?");
    parameters.add(limit);
    parameters.add(offset);
    return this;
  }

  public String buildQuery() {
    return sql.toString();
  }

  public List<Object> getParameters() {
    return parameters;
  }
}
