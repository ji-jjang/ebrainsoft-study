package com.juny.jspboardwithmybatis.global;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

@Bean
public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

  RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
    .entryTtl(Duration.ofMinutes(5));

  Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
  cacheConfigurations.put("boardDetails", defaultConfig.entryTtl(Duration.ofMinutes(1)));

  return RedisCacheManager.builder(connectionFactory)
    .cacheDefaults(defaultConfig)
    .withInitialCacheConfigurations(cacheConfigurations)
    .build();
}
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();

    template.setConnectionFactory(connectionFactory);

    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

    return template;
  }
}
