package com.jaxer.onepiece.application.gateway.ohs.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author jiaoxiangru
 * @since 2021/6/5 12:02
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ResponseBean<T> implements Serializable {
  private static final long serialVersionUID = -7934166978318617178L;

  /**
   * 响应业务码
   */
  private int code;

  /**
   * 响应描述
   */
  private String msg;

  /**
   * 响应数据
   */
  private T data;

  public ResponseBean(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public boolean success() {
    return HttpStatus.OK.value() == this.code && Objects.nonNull(this.data);
  }

  public static <T> ResponseBean<T> success(T data) {
    return new ResponseBean<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
  }

  public static <T> ResponseBean<T> fail(T data) {
    return new ResponseBean<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), data);
  }
}
