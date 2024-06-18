package cn.evlight.middleware.dynamic.thread.pool.sdk.registry;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @Description: 注册中心服务接口
 * @Author: evlight
 * @Date: 2024/6/17
 */
public interface IRegistry {

    /**
    * @Description: 上报所有线程池配置参数到注册中心
    * @Param: [threadPoolConfigEntities]
    * @return:
    * @Date: 2024/6/17
    */
    void reportThreadPoolConfigEntityList(List<ThreadPoolConfigEntity> threadPoolConfigEntities);

    /**
    * @Description: 上报单个线程池配置参数到注册中心
    * @Param: [threadPoolConfigEntity]
    * @return:
    * @Date: 2024/6/17
    */
    void reportThreadPoolConfigEntity(ThreadPoolConfigEntity threadPoolConfigEntity);

}
