package cn.evlight.middleware.dynamic.thread.pool.sdk.domain;

import cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @Description: 动态线程池服务接口
 * @Author: evlight
 * @Date: 2024/6/17
 */
public interface IDynamicThreadPoolService {

    /**
    * @Description: 获取所有线程池参数信息
    * @Param: []
    * @return:
    * @Date: 2024/6/17
    */
    List<ThreadPoolConfigEntity> getThreadPoolConfigEntityList();

    /**
    * @Description: 根据线程池名称获取线程池参数信息
    * @Param: [ThreadPoolName]
    * @return:
    * @Date: 2024/6/17
    */
    ThreadPoolConfigEntity getThreadPoolConfigEntityByName(String threadPoolName);

    /**
    * @Description: 更新线程池参数
    * @Param: [threadPoolConfigEntity]
    * @return:
    * @Date: 2024/6/17
    */
    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}
