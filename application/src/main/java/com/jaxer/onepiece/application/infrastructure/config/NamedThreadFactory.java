package com.jaxer.onepiece.application.infrastructure.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义ThreadFactory
 *
 * @author : jiaoxiangru
 * @since : 2021年05月15日
 */
@Slf4j
public class NamedThreadFactory implements ThreadFactory {

  private final AtomicInteger poolNumber = new AtomicInteger(1);

  private final ThreadGroup threadGroup;

  private final AtomicInteger threadNumber = new AtomicInteger(1);

  private final String namePrefix;

  public NamedThreadFactory(String name) {
    SecurityManager s = System.getSecurityManager();
    threadGroup = (s != null) ? s.getThreadGroup() :
        Thread.currentThread().getThreadGroup();
    if (Objects.isNull(name) || "".equals(name.trim())) {
      name = "pool";
    }
    namePrefix = name + "-" + poolNumber.getAndIncrement();
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(threadGroup, r,
        namePrefix + threadNumber.getAndIncrement(),
        0);
    log.info("NameThreadFactory,threadNumber:{}", threadNumber.get());
    if (t.isDaemon()) {
      t.setDaemon(false);
    }
    if (t.getPriority() != Thread.NORM_PRIORITY) {
      t.setPriority(Thread.NORM_PRIORITY);
    }
    return t;
  }

}
