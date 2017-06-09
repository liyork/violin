package com.wolf.utils.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Description: redis结点代理类，内部包装多个真实的redis结点对象，代表一个结点小组
 *
 * //todo-my redisConfigMap中的槽位应该是用来扩容的
 * 一个槽组名称对应一个proxy
 * 扩容，应该是将一部分的槽位放别的主-从redis，然后多个主redis之间建立集群
 */
public class RedisProxy implements Comparable<RedisProxy> {
	/**
	 * 结点小组名称
	 */
	private String slotGroup;
	/**
	 * key：槽标号，便于根据槽编号获取redis结点
	 */
	private Map<Integer,RedisReadWriteConfig> redisConfigMap = new HashMap<Integer, RedisReadWriteConfig>();
	/**
	 * 用于存放此结点小组下，所有的redis结点对象，对象不重复，方便获取所有redis结点
	 */
	private List<RedisReadWriteConfig> redisConfigList = new ArrayList<RedisReadWriteConfig>();
	
	public RedisProxy() { }
	
	public RedisProxy(String slotGroup) {
		this.slotGroup = slotGroup;
	}
    
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RedisProxy other = (RedisProxy) obj;
        if (slotGroup == null) {
            if (other.slotGroup != null)
                return false;
        } else if (!slotGroup.equals(other.slotGroup))
            return false;
        return true;
    }
	
	@Override
	public int compareTo(RedisProxy o) {

        if(this.equals(o)){
            return 0;
        }

        if(o == null || o.getSlotGroup() == null){
            return 1 ;
        }

        if(this.slotGroup == null){
            return -1 ;
        }

        return this.getSlotGroup().compareTo(o.getSlotGroup());

	}
    /**
     * 
     * Description: 向redis结点代理类中，添加redis结点实例
     * Created on 2016-7-19 下午1:38:49
     * @author
     * @param redisReadWriteConfig
     */
	public void addRedisReadWriteConfig (RedisReadWriteConfig redisReadWriteConfig) {
		this.redisConfigList.add(redisReadWriteConfig);
		
		for(int i = redisReadWriteConfig.getBeginWeight();i <= redisReadWriteConfig.getEndWeight(); i++) {
			redisConfigMap.put(i, redisReadWriteConfig);
		}
		
	}
	
	public String getSlotGroup() {
		return slotGroup;
	}

	public void setSlotGroup(String slotGroup) {
		this.slotGroup = slotGroup;
	}

	public Map<Integer, RedisReadWriteConfig> getRedisConfigMap() {
		return redisConfigMap;
	}

	public void setRedisConfigMap(Map<Integer, RedisReadWriteConfig> redisConfigMap) {
		this.redisConfigMap = redisConfigMap;
	}

	public List<RedisReadWriteConfig> getRedisConfigList() {
		return redisConfigList;
	}

	public void setRedisConfigList(List<RedisReadWriteConfig> redisConfigList) {
		this.redisConfigList = redisConfigList;
	}
	
	
}
