package com.jaxer.onepiece.application.infrastructure.config;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RateLimiter配置，主要用于限流
 *
 * @author jiaoxiangru
 * @since 2021/6/8 17:06
 */
@Slf4j
@Configuration
@SuppressWarnings("all")
public class RateLimiterConfig implements ApplicationContextAware {
  private static final String RATE_LIMITER_PERMITS_KEY = "app-bff-common.rateLimiter.permitsPerSecond";

  /**
   * RateLimiter限制QPS，可通过Apollo配置动态更新
   */
  @Value("${app-bff-common.rateLimiter.permitsPerSecond:300}")
  private int permitsPerSecond;

  private ApplicationContext applicationContext;

  private RateLimiter rateLimiter;

  @Bean
  public RateLimiter rateLimiter() {
    rateLimiter = RateLimiter.create(permitsPerSecond);
    return rateLimiter;
  }

  @ApolloConfigChangeListener(interestedKeys = {RATE_LIMITER_PERMITS_KEY})
  public void onChange(ConfigChangeEvent changeEvent) {
    this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
    updatePermits(changeEvent);
  }

  /**
   * 更新RateLimiter令牌数
   */
  public void updatePermits(ConfigChangeEvent changeEvent) {
    rateLimiter.setRate(permitsPerSecond);

    if (changeEvent.isChanged(RATE_LIMITER_PERMITS_KEY)) {
      Integer newValue = Integer.valueOf(changeEvent.getChange(RATE_LIMITER_PERMITS_KEY).getNewValue());
      log.info(RATE_LIMITER_PERMITS_KEY + "值已变动,原值:[{}],新值:[{}],rateLimiter={}",
          permitsPerSecond, newValue, JSON.toJSONString(rateLimiter));
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
