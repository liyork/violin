package com.wolf.utils.redis;

import java.io.Serializable;
import java.util.List;

/**
 * 客户端存在配置中心的配置
 * 支持一个客户端使用多个redis集群
 * ,最外层
 */
public class RedisClientConfig implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -9197543558899538974L;

	/**
     * 线程模型，0为bio，1为nio，默认为0
     */
    private byte threadType = 0;

    /**
     * 客户端每组应用的配置
     */
    private List<RedisGroupConfig> redisGroupConfigs;

    public byte getThreadType() {
        return threadType;
    }

    public void setThreadType(byte threadType) {
        this.threadType = threadType;
    }

    public List<RedisGroupConfig> getRedisGroupConfigs() {
        return redisGroupConfigs;
    }

    public void setRedisGroupConfigs(List<RedisGroupConfig> redisGroupConfigs) {
        this.redisGroupConfigs = redisGroupConfigs;
    }
}
