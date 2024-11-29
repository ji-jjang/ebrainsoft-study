package com.juny.jspboardwithmybatis.domain.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTemplateService {

  private final RedisTemplate<String, Object> redisTemplate;

  public RedisTemplateService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }
}
