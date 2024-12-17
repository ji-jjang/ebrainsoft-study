package com.juny.finalboard.global.security;

import jakarta.annotation.PostConstruct;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.stereotype.Component;

@Component
public class FilterChainDebugger {

  private final FilterChainProxy filterChainProxy;

  public FilterChainDebugger(FilterChainProxy filterChainProxy) {
    this.filterChainProxy = filterChainProxy;
  }

  /**
   *
   *
   * <h1>Security Filter 디버거 </h1>
   */
  @PostConstruct
  public void logFilters() {
    filterChainProxy
        .getFilterChains()
        .forEach(
            chain -> {
              System.out.println("Security Filter Chain:");
              chain
                  .getFilters()
                  .forEach(filter -> System.out.println(" - " + filter.getClass().getName()));
            });
  }
}
