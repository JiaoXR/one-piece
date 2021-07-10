package com.jaxer.onepiece.application.infrastructure.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 线程池核心参数Apollo配置热生效
 *
 * @author : jiaoxiangru
 * @since : 2021年05月11日22:51:30
 */
@Slf4j
@Configuration
public class ThreadPoolApplicationContextConfig implements ApplicationContextAware {
  public final static String APPLICATION_XXL_JOB_SERVICE = "applicationXxlJobService";
  private static final String APPLICATION_XXL = "application-xxl-";

  @Value("${application.xxl-job.thread-pool.core-pool-size:10}")
  private int applicationXxlJobCorePoolSize;

  private ThreadPoolExecutor applicationXxlJobThreadPoolExecutor;

  private ApplicationContext applicationContext;

  /**
   * xxl-job线程池
   */
  @Bean(APPLICATION_XXL_JOB_SERVICE)
  public ExecutorService applicationXxlJobService(BeanFactory beanFactory) {
    NamedThreadFactory threadFactory = new NamedThreadFactory(APPLICATION_XXL);
    BlockingQueue<Runnable> queue = new SynchronousQueue<>();
    applicationXxlJobThreadPoolExecutor = new ThreadPoolExecutor(
        applicationXxlJobCorePoolSize, applicationXxlJobCorePoolSize,
        0L, TimeUnit.MILLISECONDS,
        queue, threadFactory,
        new ThreadPoolExecutor.CallerRunsPolicy()
    );
    return new TraceableExecutorService(beanFactory, applicationXxlJobThreadPoolExecutor);
  }

  @ApolloConfigChangeListener(interestedKeys = {"application.xxl-job.thread-pool.core-pool-size"})
  public void onChange(ConfigChangeEvent changeEvent) {
    this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
    applicationXxlJobExecutorChange();
  }

  /**
   * xxl-job线程池
   */
  public void applicationXxlJobExecutorChange() {
    if (Objects.nonNull(applicationXxlJobThreadPoolExecutor)) {
      // refresh thread pool size
      if (applicationXxlJobCorePoolSize >= 1) {
        applicationXxlJobThreadPoolExecutor.setCorePoolSize(applicationXxlJobCorePoolSize);
        applicationXxlJobThreadPoolExecutor.setMaximumPoolSize(applicationXxlJobCorePoolSize);
      }
      log.info("application-xxl-job corePoolSize update applicationXxlJobService CorePoolSize[{}]", applicationXxlJobCorePoolSize);
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
