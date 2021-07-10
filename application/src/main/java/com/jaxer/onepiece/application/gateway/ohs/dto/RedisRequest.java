package com.jaxer.onepiece.application.gateway.ohs.dto;

import lombok.Data;

/**
 * @author jiaoxiangru
 * @since 2021/6/18 22:47
 */
@Data
public class RedisRequest {
  private String key;

  private Object value;
}
