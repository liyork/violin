/**
 * Description: RedisPool.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

import com.wolf.utils.redis.command.RedisCommands;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  redis 对象池
 * <br/> Created on 2014-7-3 下午2:34:23

 * @since 3.3
 */
public class RedisPool {
	
	//当前pool是否die
	public volatile boolean isDie = false ;
	
	public volatile boolean isRetry = false ;
	
    private LinkedBlockingQueue<PoolRedis> poolRedis ;
    
    private RedisPoolConfig config ;
    
    private AtomicInteger createCount = new AtomicInteger(0);
    
    private String name ;
    
    //标注此redis对象池是否废弃
    private volatile boolean abandon = false;
    
    public RedisPool(RedisPoolConfig config,String name){
    	this.config = config ;
    	this.poolRedis = new LinkedBlockingQueue<PoolRedis>(config.getMaximumPoolSize());
    	this.name = name ;
    }
    
    public RedisPoolConfig getConfig(){
    	return this.config;
    }
    
    public String getName(){
    	return name ;
    }
	/**
	 * 从池总获取redis
	 * 
	 * <br/> Created on 2014-7-3 下午3:59:49

	 * @since 3.3
	 * @return
	 * @throws InterruptedException
	 */
    public RedisCommands getRedis() throws InterruptedException{
    	
    	if(isDie){
    		
    		return null ;
    	}
    	
    	PoolRedis redis = null ;
    	//解决createCount 的并发问题
    	if(createCount.intValue() < config.getCorePoolSize()){
    		synchronized (this) {
        		if(createCount.intValue() < config.getCorePoolSize()){
            		redis = new Redis(config.getIp(), config.getPort(), config.getTimeout());
            		createCount.incrementAndGet();
            		redis.setUsedTime();
            		return redis ;
            	}
    		}
    	}
    	
    	
    	redis = filterRedis();			
    	if(redis != null){
    		redis.setUsedTime();
    		return redis ;
    	}
    	//解决createCount 的并发问题
    	if(createCount.intValue() < config.getMaximumPoolSize()){
    		synchronized (this) {
        		if(createCount.intValue()< config.getMaximumPoolSize()){
            		redis = new Redis(config.getIp(), config.getPort(), config.getTimeout());
            		createCount.incrementAndGet();
            		redis.setUsedTime();
            		return redis ;
            	}
    		}
    	}
    	
    	
    	redis = poolRedis.take();
    	redis.setUsedTime();
    	return redis ;
    }
    /**
     * 回收资源到redis池
     * 
     * <br/> Created on 2014-7-3 下午3:59:21

     * @since 3.3
     * @param redis
     */
    public void returnRedis(PoolRedis redis){ 
    	try {
    		if(!abandon) {
    			boolean result = poolRedis.offer(redis);
    			if(!result){
    				redis.close();
    			}
    		} else {
    			redis.close();
    		}
			
		} catch (Exception e) {
		}
    }
    /**
     * 关闭当前redis pool
     * 
     * <br/> Created on 2014-8-27 下午3:46:00

     * @since 3.4
     */
    public void close(){
    	Iterator<PoolRedis> ite = poolRedis.iterator();
    	while(ite.hasNext()){
    		PoolRedis pool = ite.next();
    		ite.remove();
    		pool.close();
    	}
    	poolRedis.clear();
    }
    
    private PoolRedis filterRedis(){
    	
    	PoolRedis redis = poolRedis.poll();
    	//
    	if(redis == null){
    		return null;
    	}
    	long thisTime = System.currentTimeMillis();
    	boolean isTimeOut = (thisTime-config.getKeepAliveTime())>redis.getLoasUsedTime()? true:false;
    	if(isTimeOut && createCount.intValue()>config.getCorePoolSize()){
    		synchronized (this) {
    			if(isTimeOut && createCount.intValue()>config.getCorePoolSize()){
    	    		createCount.decrementAndGet();
    	    		redis.close();
    	    		return filterRedis() ;
    	    	}
			}
    	}
    	
    	return redis ;
    }

	public boolean isAbandon() {
		return abandon;
	}

	public void setAbandon(boolean abandon) {
		this.abandon = abandon;
	}

	public int getCreateCount() {
		return createCount.intValue();
	}
	
    public int getIdleCount() {
    	return poolRedis.size();
    }
    
}
