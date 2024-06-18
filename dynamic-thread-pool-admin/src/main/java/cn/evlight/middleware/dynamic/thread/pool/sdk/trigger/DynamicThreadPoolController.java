package cn.evlight.middleware.dynamic.thread.pool.sdk.trigger;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.evlight.middleware.dynamic.thread.pool.sdk.trigger.dto.request.AdjustThreadPoolConfigRequestDTO;
import cn.evlight.middleware.dynamic.thread.pool.sdk.types.Response;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 动态线程池api
 * @Author: evlight
 * @Date: 2024/6/17
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/dynamic/thread/pool")
public class DynamicThreadPoolController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/query_thread_pool_list")
    public Response<List<ThreadPoolConfigEntity>> queryThreadPoolList() {
        log.info("[request]-[查询线程池配置参数列表]");
        try {
            RList<ThreadPoolConfigEntity> cacheList = redissonClient.getList("thread_pool_config_list_key");
            log.info("[request]-[查询线程池配置参数列表] 成功");
            return Response.success(cacheList.readAll());
        } catch (Exception e) {
            log.error("[request]-[查询线程池配置参数列表] 失败");
            return Response.error();
        }
    }

    @GetMapping(value = "/query_thread_pool_config")
    public Response<ThreadPoolConfigEntity> queryThreadPoolConfig(@RequestParam String appName, @RequestParam String threadPoolName) {
        log.info("[request]-[根据应用名称和线程池名称查询线程池配置参数]");
        try {
            String cacheKey = "thread_pool_config_key" + ":" + appName + ":" + threadPoolName;
            ThreadPoolConfigEntity threadPoolConfigEntity = redissonClient.<ThreadPoolConfigEntity>getBucket(cacheKey).get();
            log.info("[request]-[根据应用名称和线程池名称查询线程池配置参数] 成功");
            return Response.success(threadPoolConfigEntity);
        } catch (Exception e) {
            log.error("[request]-[根据应用名称和线程池名称查询线程池配置参数] 失败");
            return Response.error();
        }
    }

    @PostMapping(value = "/update_thread_pool_config")
    public Response<Boolean> adjustThreadPoolConfig(@RequestBody AdjustThreadPoolConfigRequestDTO request) {
        log.info("[request]-[修改线程池配置参数]");
        try {
            RTopic topic = redissonClient.getTopic("dynamic_thread_pool_redis_topic" + ":" + request.getAppName());
            ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity();
            threadPoolConfigEntity.setAppName(request.getAppName());
            threadPoolConfigEntity.setThreadPoolName(request.getThreadPoolName());
            threadPoolConfigEntity.setCorePoolSize(request.getCorePoolSize());
            threadPoolConfigEntity.setMaximumPoolSize(request.getMaximumPoolSize());
            threadPoolConfigEntity.setActiveCount(request.getActiveCount());
            threadPoolConfigEntity.setPoolSize(request.getPoolSize());
            threadPoolConfigEntity.setQueueType(request.getQueueType());
            threadPoolConfigEntity.setQueueSize(request.getQueueSize());
            threadPoolConfigEntity.setRemainingCapacity(request.getRemainingCapacity());
            topic.publish(threadPoolConfigEntity);
            log.info("[request]-[修改线程池配置参数] 成功");
            return Response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("[request]-[修改线程池配置参数] 失败");
            return Response.success(Boolean.FALSE);
        }
    }

}
