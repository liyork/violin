package com.wolf.utils.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每一个redis集群，对应cachecloud里面的应用
 */
public class RedisCluster {
    /**
     * 0为普通哈希，1为一致性哈希
     */
	private int hashStrategy = 0;

    /**
     * 集群（应用）下面的小集群，通过slotGroupName来排序
     */
	private List<RedisProxy> redisProxyList = new ArrayList<RedisProxy>();

    /**
     * key为slotGroupName，value为小集群
     */
	private Map<String,RedisProxy> redisProxyMap = new HashMap<String, RedisProxy>();
	
	/**
	 * 一致性hash缓存 以 slotGroupName分配槽
	 */
	private ConsistentHashingInstance consistentHashing = null;
	
	public RedisCluster() {}
	
	public RedisCluster(int hashStrategy) {
		this.hashStrategy = hashStrategy;
	}
	

	public int getHashStrategy() {
		return hashStrategy;
	}

	public void setHashStrategy(int hashStrategy) {
		this.hashStrategy = hashStrategy;
	}

	public List<RedisProxy> getRedisProxyList() {
		return redisProxyList;
	}

	public void setRedisProxyList(List<RedisProxy> redisProxyList) {
		this.redisProxyList = redisProxyList;
	}

	public Map<String, RedisProxy> getRedisProxyMap() {
		return redisProxyMap;
	}

	public void setRedisProxyMap(Map<String, RedisProxy> redisProxyMap) {
		this.redisProxyMap = redisProxyMap;
	}

	public ConsistentHashingInstance getConsistentHashing() {
		return consistentHashing;
	}

	public void setConsistentHashing(ConsistentHashingInstance consistentHashing) {
		this.consistentHashing = consistentHashing;
	}
	/**
	 * 
	 * Description: 向redis集群中，添加redis结点实例
	 * Created on 2016-7-19 下午1:36:49
	 * @author
	 * @param redisReadWriteConfig
	 */
	public synchronized void addProxy(RedisReadWriteConfig redisReadWriteConfig) {
		RedisProxy redisProxy = this.redisProxyMap.get(redisReadWriteConfig.getSlotGroup());
		if(redisProxy == null) {
			redisProxy = new RedisProxy(redisReadWriteConfig.getSlotGroup());
			this.redisProxyMap.put(redisReadWriteConfig.getSlotGroup(), redisProxy);
			this.redisProxyList.add(redisProxy);
		}
		redisProxy.addRedisReadWriteConfig(redisReadWriteConfig);
	}
	/**
	 * 
	 * Description: 初始化一致性hash
	 * Created on 2016-7-19 下午1:37:30
	 * @author
	 */
	public void initConsistentHashing () {
		List<Node> nodes = new ArrayList<Node>();
		for(RedisProxy redisProxy : redisProxyList) {
			nodes.add(new Node(redisProxy.getSlotGroup())) ;
		}
		
		consistentHashing = new ConsistentHashingInstance(nodes, HashAlgorithm.KETAMA_HASH);
	}
	/**
	 * 
	 * Description: 获取集群下，所有的redis结点实例
	 * Created on 2016-7-19 下午1:36:10
	 * @author
	 * @return
	 */
	public List<RedisReadWriteConfig> getAllRedisReadWriteConfig() {
		List<RedisReadWriteConfig> listConf = new ArrayList<RedisReadWriteConfig>();
    	for(RedisProxy proxy: redisProxyList) {
    		listConf.addAll(proxy.getRedisConfigList());
    	}
    	return listConf;
	}
	
}
