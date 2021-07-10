package com.jaxer.onepiece.application.gateway.ohs.controller;

import com.jaxer.onepiece.application.application.LocalCacheService;
import com.jaxer.onepiece.application.gateway.ohs.dto.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiaoxiangru
 * @since 2021/7/10 15:02
 */
@RestController
@RequestMapping("v1/cache")
public class CacheController {
  @Autowired
  private LocalCacheService localCacheService;

  @GetMapping("add")
  public ResponseBean<Object> get(String key, String value) {
    return ResponseBean.success(localCacheService.addCache(key, value));
  }
}
