package cn.evlight.middleware.dynamic.thread.pool.sdk.trigger.listener;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.evlight.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description: 修改线程池参数监听
 * @Author: evlight
 * @Date: 2024/6/17
 */
public class AdjustThreadPoolConfigListener implements MessageListener<ThreadPoolConfigEntity> {

    private final Logger logger = LoggerFactory.getLogger(AdjustThreadPoolConfigListener.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public AdjustThreadPoolConfigListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("[修改线程池参数监听] 监听到修改消息");
        try {
            dynamicThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);
            //更新后直接更新注册中心上的数据
            List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.getThreadPoolConfigEntityList();
            registry.reportThreadPoolConfigEntityList(threadPoolConfigEntities);
            registry.reportThreadPoolConfigEntity(threadPoolConfigEntity);
            logger.info("[修改线程池参数监听] 成功");
        } catch (Exception e) {
            logger.error("[修改线程池参数监听] 失败");
        }
    }
}
