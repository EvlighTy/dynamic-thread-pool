package cn.evlight.middleware.dynamic.thread.pool.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: redis配置参数
 * @Author: evlight
 * @Date: 2024/6/18
 */
@ConfigurationProperties(prefix = "dynamic.thread.pool.config.redis", ignoreInvalidFields = true)
public class RedisConfigProperties {
    private boolean enabled;
    private String host; //redis address
    private int port; //redis port
    private String password; //账密
    private int poolSize = 64; //连接池大小
    private int minIdleSize = 10; //最小空闲连接数
    private int idleTimeout = 10000; //连接空闲时间
    private int connectTimeout = 10000; //连接超时时间
    private int retryAttempts = 3; //连接重试次数
    private int retryInterval = 1000; //连接重试间隔时
    private int pingInterval = 0; //检查连接是否可用间隔时间 0=不检查
    private boolean keepAlive = true; //是否保持长连接

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getMinIdleSize() {
        return minIdleSize;
    }

    public void setMinIdleSize(int minIdleSize) {
        this.minIdleSize = minIdleSize;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(int pingInterval) {
        this.pingInterval = pingInterval;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
