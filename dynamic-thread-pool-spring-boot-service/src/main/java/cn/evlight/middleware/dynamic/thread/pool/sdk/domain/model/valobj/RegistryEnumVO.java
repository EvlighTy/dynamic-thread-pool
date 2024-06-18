package cn.evlight.middleware.dynamic.thread.pool.sdk.domain.model.valobj;

/**
 * @Description: 注册中心枚举值对象
 * @Author: evlight
 * @Date: 2024/6/17
 */
public enum RegistryEnumVO {

    THREAD_POOL_CONFIG_LIST_KEY("thread_pool_config_list_key", "线程池配置参数列表"),
    THREAD_POOL_CONFIG_KEY("thread_pool_config_key", "线程池配置参数"),
    DYNAMIC_THREAD_POOL_TOPIC("dynamic_thread_pool_redis_topic", "动态线程池监听主题");

    private final String key;
    private final String desc;

    RegistryEnumVO(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

}
