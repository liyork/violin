package com.wolf.utils.redis;

import java.io.Serializable;

/**

 * 客户端存放在配置中心配置中对应的集群配置
 */
public class RedisGroupConfig implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 连接池配置信息
     */
    private RedisPoolConfig redisPoolConfig;
    /**
     * 应用名称，与缓存云中定义应用名称对应
     */
    private String appName;

    /**
     * 组名称，本地项目通过api注入的组，如果为空，则返回appName
     */
    private String groupName;

    public RedisPoolConfig getRedisPoolConfig() {
        return redisPoolConfig;
    }

    public void setRedisPoolConfig(RedisPoolConfig redisPoolConfig) {
        this.redisPoolConfig = redisPoolConfig;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getGroupName() {
        if(groupName == null || groupName.equals("")) {
            return appName;
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
