/**
 * Description: DynamicReentrantZkLock.java
 * All Rights Reserved.

 */
package com.wolf.utils.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  动态 分布式锁实现
 * <br/> Created on 2013-11-28 下午3:47:19

 * @since 3.2 
 */
public class DynamicReentrantZkLock extends ReentrantZkLock {
	
	private static final Logger LOG = LoggerFactory.getLogger(DynamicReentrantZkLock.class);
	
	public DynamicReentrantZkLock(String baseNode, ZkSessionManager zkSessionManager){
		super(baseNode,zkSessionManager);
	}

	@Override
	public void unlock() {
		
		super.unlock();
		 LockHolder nodeToRemove = locks.get();
	        if(nodeToRemove==null){
	        	try {
					ZkInternalUtils.safeDelete(zkSessionManager.getZooKeeper(),baseNode,-1);
				} catch (KeeperException.NotEmptyException e) {
					LOG.info("移除动态节点不为空,"+e.getMessage());
				} catch (Exception e) {
					LOG.error("",e);
				}
	        }
		
	}
	
}
