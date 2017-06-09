/**
 * Description: RedisConfig.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

/**
 *  redis 参数配置
 * <br/> Created on 2014-7-3 下午3:00:45

 * @since 3.3
 */
public class RedisConfig {
	
	private String name;
	
	private String groupName ;
	
	private String rwName;
	
	private RedisPoolConfig poolConfig;
    /**
     * 0为普通哈希，1为一致性哈希
     */
	private int hashStrategy = 0;
    /**
     * slot分组名称
     */
    private String slotGroup;
	
    /**
     * 权重，值应该为从0-99
     */
    private String weight = "0-99";
	
	public String getRwName() {
		return rwName;
	}

	public void setRwName(String rwName) {
		this.rwName = rwName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public RedisPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(RedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public int getHashStrategy() {
		return hashStrategy;
	}

	public void setHashStrategy(int hashStrategy) {
		this.hashStrategy = hashStrategy;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getSlotGroup() {
		if(slotGroup == null) {
			return name;
		}
		return slotGroup;
	}

	public void setSlotGroup(String slotGroup) {
		this.slotGroup = slotGroup;
	}
	
}
