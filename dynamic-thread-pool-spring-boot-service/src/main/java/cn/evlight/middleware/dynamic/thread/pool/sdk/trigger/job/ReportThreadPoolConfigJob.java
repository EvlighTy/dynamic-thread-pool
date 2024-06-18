package cn.evlight.middleware.dynamic.thread.pool.sdk.trigger.job;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.evlight.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @Description: 上传线程池信息定时任务
 * @Author: evlight
 * @Date: 2024/6/17
 */
public class ReportThreadPoolConfigJob {

    private final Logger logger = LoggerFactory.getLogger(ReportThreadPoolConfigJob.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ReportThreadPoolConfigJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void exec(){
        logger.info("[Scheduled Task]-[上报线程池配置参数信息到注册中心]");
        try {
            List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.getThreadPoolConfigEntityList();
            registry.reportThreadPoolConfigEntityList(threadPoolConfigEntities);
            for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
                registry.reportThreadPoolConfigEntity(threadPoolConfigEntity);
            }
            logger.info("[Scheduled Task]-[上报线程池配置参数信息到注册中心] 成功");
        } catch (Exception e) {
            logger.error("[Scheduled Task]-[上报线程池配置参数信息到注册中心] 失败");
        }
    }
}
