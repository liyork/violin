/**
 * Description: RedisFactory.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;


import com.wolf.utils.redis.command.*;

/**
 *  redis 工厂方法
 *  用于获取实例对象，进行操作
 * <br/> Created on 2014-7-4 上午10:01:45

 * @since 3.3
 */
public class RedisFactory {
	/**
	 * 获取当前组下集群下的对象
	 * 
	 * <br/> Created on 2014-7-16 下午2:57:30

	 * @since 3.3
	 * @param groupName
	 * @return
	 */
	public static final AtomicCommands getClusterAtomicCommands(String groupName){
		return new DefaultAtomicCommands(groupName);
	}
	
	public static final HashCommands getClusterHashCommands(String groupName){
		return new DefaultHashCommands(groupName);
	}
	
	public static final ListCommands getClusterListCommands(String groupName){
		return new DefaultListCommands(groupName);
	}
	
	public static final SetCommands getClusterSetCommands(String groupName){
		return new DefaultSetCommands(groupName);
	}
	
	public static final StringCommands getClusterStringCommands(String groupName){
		return new DefaultStringCommands(groupName);
	}
	
	public static final Commands getClusterTedisManager(String groupName){
		return new DefaultCommands(groupName);
	}
	
	public static final ValueCommands getClusterValueCommands(String groupName){
		return new DefaultValueCommands(groupName);
	}
	
	public static final ZSetCommands getClusterZSetCommands(String groupName){
		return new DefaultZSetCommands(groupName);
	}
	
	/**
	 * 获取指定组下，指定redis 的对象
	 * 
	 * <br/> Created on 2014-7-16 下午2:58:18

	 * @since 3.3
	 * @param groupName
	 * @param name
	 * @return
	 */
	public static final AtomicCommands getClusterAtomicCommands(String groupName ,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultAtomicCommands(groupName);
	}
	
	public static final HashCommands getClusterHashCommands(String groupName,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultHashCommands(groupName);
	}
	
	public static final ListCommands getClusterListCommands(String groupName,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultListCommands(groupName);
	}
	
	public static final SetCommands getClusterSetCommands(String groupName,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultSetCommands(groupName);
	}
	
	public static final StringCommands getClusterStringCommands(String groupName,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultStringCommands(groupName);
	}
	
	public static final Commands getClusterTedisManager(String groupName,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultCommands(groupName);
	}
	
	public static final ValueCommands getClusterValueCommands(String groupName,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultValueCommands(groupName);
	}
	
	public static final ZSetCommands getClusterZSetCommands(String groupName,String name){
		setAssignRedisPoolContext(groupName, name);
		return new DefaultZSetCommands(groupName);
	}
	
	private static final void setAssignRedisPoolContext(String groupName,String name){
		RedisPool pool = ClusterManager.clusterModelHash(groupName, name,null);
		AssignRedisPool.ASSIGN_REDIS_POOL_CONTEXT.set(pool);
	}
	
	
	
	
}
