package com.jaxer.onepiece.application.repositories;

import com.jaxer.onepiece.application.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiaoxiangru
 * @since 2021/5/23 11:45
 */
@Mapper
@Repository
public interface UserMapper {
  /**
   * 插入一条记录
   *
   * @param user User实体类
   * @return 自增主键
   */
  int insert(User user);

  /**
   * 批量插入
   *
   * @param userList 用户列表
   */
  void batchInsert(List<User> userList);

  /**
   * 根据id查询
   *
   * @param id 自增主键
   * @return User实体类
   */
  User findById(Integer id);
}
