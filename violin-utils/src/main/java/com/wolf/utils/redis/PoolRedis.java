/**
 * Description: PoolRedis.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

import com.wolf.utils.redis.command.RedisCommands;

/**
 *  
 * <br/> Created on 2014-7-3 下午2:51:49

 * @since 3.3
 */
public abstract class PoolRedis implements RedisCommands {
	
	protected long lastUsedTime ;
	
	public abstract void setUsedTime();
	
	public long getLoasUsedTime(){
		return lastUsedTime;
	}
	
	public abstract void close();
	

}
