package cn.evlight.middleware.dynamic.thread.pool.sdk.registry.redis;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import cn.evlight.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;

/**
 * @Description: 注册中心服务redis实现类
 * @Author: evlight
 * @Date: 2024/6/17
 */
public class RedisRegistry implements IRegistry {

    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPoolConfigEntityList(List<ThreadPoolConfigEntity> threadPoolConfigEntities) {
        String key = RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey();
        RList<ThreadPoolConfigEntity> list = redissonClient.getList(key);
        list.delete();
        list.addAll(threadPoolConfigEntities);
    }

    @Override
    public void reportThreadPoolConfigEntity(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String key = RegistryEnumVO.THREAD_POOL_CONFIG_KEY.getKey() + ":" + threadPoolConfigEntity.getAppName() + ":" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(key);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(1));
    }
}
