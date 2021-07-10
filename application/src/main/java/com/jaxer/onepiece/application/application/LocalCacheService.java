package com.jaxer.onepiece.application.application;

import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiaoxiangru
 * @since 2021/7/10 15:03
 */
@Service
public class LocalCacheService {
  @Autowired
  private Cache<String, Object> localCache;

  public Boolean addCache(String key, Object value) {
    localCache.put(key, value);
    return Boolean.TRUE;
  }
}
