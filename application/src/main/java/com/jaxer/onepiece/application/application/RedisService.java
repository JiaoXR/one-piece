package com.jaxer.onepiece.application.application;

import com.jaxer.onepiece.application.infrastructure.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static com.jaxer.onepiece.application.infrastructure.constant.RedisConstant.DEFAULT_EXPIRED_TIME_SECOND;

/**
 * @author jiaoxiangru
 * @since 2021/6/18 22:48
 */
@Slf4j
@Service
public class RedisService {
  @Autowired
  private RedisTemplate<String, Object> redisTemplate;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  public void set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRED_TIME_SECOND, TimeUnit.SECONDS);
  }

  public Boolean stringTemplateSet(String key, String value) {
    log.info("lock02, key={}, value={}", key, value);
    return stringRedisTemplate.opsForValue().setIfAbsent(key, value, DEFAULT_EXPIRED_TIME_SECOND, TimeUnit.SECONDS);
  }

  public Boolean setIfAbsent(String key, Object value) {
    log.info("setIfAbsent==> key={}, value={}", key, value);
    return redisTemplate.opsForValue().setIfAbsent(key, value, DEFAULT_EXPIRED_TIME_SECOND, TimeUnit.SECONDS);
  }

  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public Long release(String key, Object value) {
    Object existedValue = redisTemplate.opsForValue().get(key);
    log.info("key:{}, value:{}, redis旧值：{}", key, value, existedValue);

//    Object existedValue = stringRedisTemplate.opsForValue().get(key);
//    log.info("key:{}, value:{}, redis旧值：{}", key, value, existedValue);

    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RedisConstant.COMPARE_AND_DELETE, Long.class);
    return redisTemplate.execute(redisScript, Collections.singletonList(key), value);
  }
}
