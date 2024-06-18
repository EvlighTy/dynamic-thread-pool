package cn.evlight.middleware.dynamic.thread.pool.sdk.domain;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 动态线程池服务实现类
 * @Author: evlight
 * @Date: 2024/6/17
 */
public class DynamicThreadPoolService implements IDynamicThreadPoolService{

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolService.class);

    private final String applicationName;

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public DynamicThreadPoolService(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public List<ThreadPoolConfigEntity> getThreadPoolConfigEntityList() {
        logger.info("[查询线程池参数信息列表]");
        ArrayList<ThreadPoolConfigEntity> threadPoolConfigEntities = new ArrayList<>(threadPoolExecutorMap.size());
        for (String threadPoolName : threadPoolExecutorMap.keySet()) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
            ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity();
            threadPoolConfigEntity.setAppName(applicationName);
            threadPoolConfigEntity.setThreadPoolName(threadPoolName);
            threadPoolConfigEntity.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
            threadPoolConfigEntity.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
            threadPoolConfigEntity.setActiveCount(threadPoolExecutor.getActiveCount());
            threadPoolConfigEntity.setPoolSize(threadPoolExecutor.getPoolSize());
            threadPoolConfigEntity.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
            threadPoolConfigEntity.setQueueSize(threadPoolExecutor.getQueue().size());
            threadPoolConfigEntity.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());
            threadPoolConfigEntities.add(threadPoolConfigEntity);
        }
        return threadPoolConfigEntities;
    }

    @Override
    public ThreadPoolConfigEntity getThreadPoolConfigEntityByName(String threadPoolName) {
        logger.info("[根据线程池名称查询线程池参数]");
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if(threadPoolExecutor == null){
            //根据线程池名称找不到对应线程池
            logger.error("[根据线程池名称查询线程池参数] 线程池不存在");
            return new ThreadPoolConfigEntity(applicationName, threadPoolName);
        }
        ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity();
        threadPoolConfigEntity.setAppName(applicationName);
        threadPoolConfigEntity.setThreadPoolName(threadPoolName);
        threadPoolConfigEntity.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
        threadPoolConfigEntity.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
        threadPoolConfigEntity.setActiveCount(threadPoolExecutor.getActiveCount());
        threadPoolConfigEntity.setPoolSize(threadPoolExecutor.getPoolSize());
        threadPoolConfigEntity.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
        threadPoolConfigEntity.setQueueSize(threadPoolExecutor.getQueue().size());
        threadPoolConfigEntity.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());
        return threadPoolConfigEntity;
    }

    @Override
    public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("[修改线程池参数]");
        if(StringUtils.isBlank(threadPoolConfigEntity.getThreadPoolName()) || !threadPoolConfigEntity.getAppName().equals(applicationName)){
            logger.error("[修改线程池参数] 传递参数不合法");
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (threadPoolExecutor == null){
            logger.error("[修改线程池参数] 线程不存在");
            return;
        }
        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
    }

}
