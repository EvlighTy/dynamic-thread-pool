package cn.evlight.thread.pool.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 线程池配置参数
 * @Author: evlight
 * @Date: 2024/6/17
 */
@ConfigurationProperties(prefix = "thread.pool.executor.config", ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {
    private Integer corePoolSize = 20; //核心线程数
    private Integer maxPoolSize = 200; //最大线程数
    private Long keepAliveTime = 30L; //线程存活时间
    private Integer blockQueueSize = 1000; //阻塞队列大小
    private String policy = "AbortPolicy"; //任务拒绝策略

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getBlockQueueSize() {
        return blockQueueSize;
    }

    public void setBlockQueueSize(Integer blockQueueSize) {
        this.blockQueueSize = blockQueueSize;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
