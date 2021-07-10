package com.jaxer.onepiece.application.infrastructure.constant;

/**
 * @author jiaoxiangru
 * @since 2021/6/27 19:16
 */
public class RedisConstant {
  /**
   * 默认Redis缓存过期时间
   */
  public static final long DEFAULT_EXPIRED_TIME_SECOND = 60 * 60;

  /**
   * Redis释放锁Lua脚本
   */
  public static final String COMPARE_AND_DELETE =
      "if redis.call('get',KEYS[1]) == ARGV[1]\n"
          + "then\n"
          + "    return redis.call('del',KEYS[1])\n"
          + "else\n"
          + "    return 0\n"
          + "end";
}
