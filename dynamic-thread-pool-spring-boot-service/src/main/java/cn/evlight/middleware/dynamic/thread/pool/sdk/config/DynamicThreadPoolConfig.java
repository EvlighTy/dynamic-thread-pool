package cn.evlight.middleware.dynamic.thread.pool.sdk.config;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.DynamicThreadPoolService;
import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import cn.evlight.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import cn.evlight.middleware.dynamic.thread.pool.sdk.registry.redis.RedisRegistry;
import cn.evlight.middleware.dynamic.thread.pool.sdk.trigger.listener.AdjustThreadPoolConfigListener;
import cn.evlight.middleware.dynamic.thread.pool.sdk.trigger.job.ReportThreadPoolConfigJob;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 动态线程池配置类
 * @Author: evlight
 * @Date: 2024/6/17
 */
@EnableScheduling
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolConfigProperties.class)
public class DynamicThreadPoolConfig {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolConfig.class);

    private String applicationName;

    //注册中心
    @Bean
    public RedissonClient redissonClient(DynamicThreadPoolConfigProperties properties){
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive());
        RedissonClient redissonClient = Redisson.create(config);
        logger.info("[动态线程池配置] 注册中心redis初始化完成");
        return redissonClient;
    }

    //注册中心服务
    @Bean
    public IRegistry redisRegistry(RedissonClient redissonClient){
        return new RedisRegistry(redissonClient);
    }

    //动态线程池服务
    @Bean
    public IDynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap, RedissonClient redissonClient){
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if(StringUtils.isBlank(applicationName)){
            applicationName = "null";
            logger.error("[动态线程池配置] 应用名称为空");
        }
        //检查线程池配置参数之前是否注册到过注册中心
        for (String threadPoolName : threadPoolExecutorMap.keySet()) {
            String key = RegistryEnumVO.THREAD_POOL_CONFIG_KEY + ":" + applicationName + ":" + threadPoolName;
            RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(key);
            ThreadPoolConfigEntity threadPoolConfigEntity = bucket.get();
            if (threadPoolConfigEntity == null){
                continue;
            }
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
            threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
            threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
        }
        return new DynamicThreadPoolService(applicationName, threadPoolExecutorMap);
    }

    //定时任务
    @Bean
    public ReportThreadPoolConfigJob reportThreadPoolConfigJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry){
        return new ReportThreadPoolConfigJob(dynamicThreadPoolService, registry);
    }

    //消费者
    @Bean
    public AdjustThreadPoolConfigListener adjustThreadPoolConfigListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry){
        return new AdjustThreadPoolConfigListener(dynamicThreadPoolService, registry);
    }

    //消息队列
    @Bean
    public RTopic dynamicThreadPoolRedisTopic(RedissonClient redissonClient, AdjustThreadPoolConfigListener adjustThreadPoolConfigListener){
        String topicKey = RegistryEnumVO.DYNAMIC_THREAD_POOL_TOPIC.getKey() + ":" + applicationName;
        RTopic topic = redissonClient.getTopic(topicKey);
        topic.addListener(ThreadPoolConfigEntity.class, adjustThreadPoolConfigListener);
        return topic;
    }

}
