package com.ecommerce.global.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
  private final StringRedisTemplate redisTemplate;

  public String getData(String key) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    return ops.get(key);
  }

  public void setData(String key, String value) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.set(key, value);
  }

  public void setDataExpire(String key, String value, long duration) {
    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    Duration expire = Duration.ofSeconds(duration);
    ops.set(key, value, expire);
  }

  public void deleteData(String key) {
    redisTemplate.delete(key);
  }
}
