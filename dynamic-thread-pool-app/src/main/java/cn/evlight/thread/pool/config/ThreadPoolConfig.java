package cn.evlight.thread.pool.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Description: 线程池配置
 * @Author: evlight
 * @Date: 2024/6/17
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class ThreadPoolConfig {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Bean("threadPoolExecutor_01")
    public ThreadPoolExecutor threadPoolExecutor_01(ThreadPoolConfigProperties properties){
        logger.info("[线程池初始化]-[threadPoolExecutor_01]");
        RejectedExecutionHandler rejectedExecutionHandler;
        switch (properties.getPolicy()){
            case "AbortPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case "CallerRunsPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            case "DiscardPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case "DiscardOldestPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            default:
                rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
                break;
        }
        return new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
    }

    @Bean("threadPoolExecutor_02")
    public ThreadPoolExecutor threadPoolExecutor_02(ThreadPoolConfigProperties properties){
        logger.info("[线程池初始化]-[threadPoolExecutor_02]");
        RejectedExecutionHandler rejectedExecutionHandler;
        switch (properties.getPolicy()){
            case "AbortPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case "CallerRunsPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            case "DiscardPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case "DiscardOldestPolicy":
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            default:
                rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
                break;
        }
        return new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
    }

}
