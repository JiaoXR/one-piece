package com.jaxer.onepiece.application.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author jiaoxiangru
 * @since 2021/5/23 12:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private Long id;

  private String name;

  private String enName;

  private String nickname;

  private Integer age;

  private Date createdAt;

  private Date updatedAt;
}
