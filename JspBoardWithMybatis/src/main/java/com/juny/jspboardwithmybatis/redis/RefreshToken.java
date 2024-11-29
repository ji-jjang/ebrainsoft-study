package com.juny.jspboardwithmybatis.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "RefreshToken", timeToLive = 60)
@Getter
public class RefreshToken {

  @Id private Long id;
  private Long userId;
  private String refresh;

  public RefreshToken(Long id, Long userId, String refresh) {
    this.id = id;
    this.userId = userId;
    this.refresh = refresh;
  }
}
