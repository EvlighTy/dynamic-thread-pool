package cn.evlight.middleware.dynamic.thread.pool.sdk.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: redis配置类
 * @Author: evlight
 * @Date: 2024/6/18
 */
@Configuration
@EnableConfigurationProperties(RedisConfigProperties.class)
public class RedisConfig {

    private final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Bean
    public RedissonClient redissonClient(RedisConfigProperties properties){
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
        logger.info("[redis] 初始化完成");
        return redissonClient;
    }
}
