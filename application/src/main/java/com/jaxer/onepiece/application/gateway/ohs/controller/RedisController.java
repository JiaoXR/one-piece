package com.jaxer.onepiece.application.gateway.ohs.controller;

import com.jaxer.onepiece.application.application.RedisService;
import com.jaxer.onepiece.application.gateway.ohs.dto.RedisRequest;
import com.jaxer.onepiece.application.gateway.ohs.dto.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiaoxiangru
 * @since 2021/6/18 22:43
 */
@RestController
@RequestMapping("v1/redis")
public class RedisController {
  @Autowired
  private RedisService redisService;

  @GetMapping("{redisKey}")
  public ResponseBean<Object> get(@PathVariable("redisKey") String redisKey) {
    return ResponseBean.success(redisService.get(redisKey));
  }

  @PostMapping("addCache")
  public ResponseBean<Boolean> add(@RequestBody RedisRequest request) {
    redisService.set(request.getKey(), request.getValue());
    return ResponseBean.success(true);
  }

  @GetMapping("/redis/lock01")
  public Object redis001(String key, String value) {
    return ResponseBean.success(redisService.setIfAbsent(key, value));
  }

  @GetMapping("/redis/lock02")
  public Object redis002(String key, String value) {
    return ResponseBean.success(redisService.stringTemplateSet(key, value));
  }

  @GetMapping("/redis/release")
  public Object release(String key, String value) {
    Long result = redisService.release(key, value);
    return ResponseBean.success(result > 0);
  }
}
