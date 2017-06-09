package com.wolf.utils.redis;

import java.util.List;

/**
 * 与配置中心对应对象
 */
public class RedisAppConfig {

    /**
     * 应用名称，与目前的group名称对应
     */
    private String appName;
    /**
     * 连接池配置
     */
    private RedisPoolConfig poolConfig ;
    /**
     * server的配置
     */
    private List<RedisServerConfig> serverConfig;

    /**
     * 数据状态
     * 0为正常
     * 1为扩容
     * -1为缩容
     */
    private int dataStatus = 0;

    /**
     * 监控类型，默认为0
     * 0为zk监控
     * 1为cachecloud监控
     */
    private int monitorType = 0;

    /**
     * 集群模式
     * 0为单点或者多主
     * 1为集群或者主备
     * ...
     */
    private int appType = 0;

    /**
     * 0为普通哈希，1为一致性哈希
     */
    private int hashStrategy = 0;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public synchronized RedisPoolConfig getPoolConfig() {
        if(poolConfig == null) {
            poolConfig = new RedisPoolConfig();
        }
        return poolConfig;
    }

    public void setPoolConfig(RedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public List<RedisServerConfig> getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(List<RedisServerConfig> serverConfig) {
        this.serverConfig = serverConfig;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }


    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public int getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(int monitorType) {
        this.monitorType = monitorType;
    }

    public int getHashStrategy() {
        return hashStrategy;
    }

    public void setHashStrategy(int hashStrategy) {
        this.hashStrategy = hashStrategy;
    }
}
