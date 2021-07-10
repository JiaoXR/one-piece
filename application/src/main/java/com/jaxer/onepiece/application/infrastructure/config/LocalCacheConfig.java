package com.jaxer.onepiece.application.infrastructure.config;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 *
 * @author jiaoxiangru
 * @since 2021/7/1 17:32
 */
@Slf4j
@Configuration
public class LocalCacheConfig {
  /**
   * 围栏过期时间，默认30秒
   */
  @Value("${fence.cache.expired.second:30}")
  public long expiredSecond;

  /**
   * 围栏本地缓存最大数量，默认3000
   */
  @Value("${fence.cache.max.size:3000}")
  public long maxSize;

  @Bean
  public Cache<String, Object> localCache() {
    return CacheBuilder.newBuilder()
        .expireAfterWrite(expiredSecond, TimeUnit.SECONDS)
        .maximumSize(maxSize)
        .removalListener((RemovalListener<String, Object>) notification -> {
          String key = notification.getKey();
          Object value = notification.getValue();
          RemovalCause cause = notification.getCause();
          log.info("CacheBuilder被移除了, cause={}, key={}, value={}", cause, key, JSON.toJSONString(value));
        })
        .build();
  }
}
