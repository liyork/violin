/**
 * Description: AssignRedisPool.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

/**
 *  存储    指  定redispool 上下文类 
 * <br/> Created on 2014-7-16 下午2:53:23

 * @since 3.3
 */
public class AssignRedisPool {
	/**
	 * 存储当前上下文redispool
	 */
	public static final ThreadLocal<RedisPool> ASSIGN_REDIS_POOL_CONTEXT = new ThreadLocal<RedisPool>(); 
	
}
