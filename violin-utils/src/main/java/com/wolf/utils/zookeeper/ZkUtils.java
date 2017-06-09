package com.wolf.utils.zookeeper;

import com.wolf.utils.PropertiesReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * 
 *  使用zk 工具方法 All Rights Reserved.
 * <br/> Created on 2013-9-18 上午10:47:25

 * @since 3.2
 */
public final class ZkUtils {
	
	public static final String PROJECT_PREFIX = initPrefix();
	
	//初始化zksessionmanager
	public  static final ZkSessionManager ZK_SESSION_MANAGER = init();
	
	//初始化zksessionmanager to B
	public  static final ZkSessionManager ZK_SESSION_MANAGER_B = initB();

	//分布式锁默认路径
	public static final String BASE_DIR_LOCK = ZkUtils.PROJECT_PREFIX+"/lock/";
	
	//应用依赖的framework版本路径
	public static final String MAVEN_VERSION_PATH = ZkUtils.PROJECT_PREFIX + "/projectVersion";
	
	private static final int DEFAULT_COUNT = 5;
	
	private static final int DEFAULT_MAX = 50 ;
	
	private static final int THREAD_SLEEP = 10;

    private static final Logger LOG = LoggerFactory.getLogger(ZkUtils.class);
    //缓存同一个path 下的分布式锁
    private static final ConcurrentHashMap<String, Lock> LOCK_MAP = new ConcurrentHashMap<String, Lock>();
    
    private static Map<String, ZkSessionManager> zkSessionManagerMap = new ConcurrentHashMap<String, ZkSessionManager>();
    
  	private static Object obj = new Object();
    

    private ZkUtils() {
    }
    /**
     * 判断当前zk 是否在有权限访问的acl
     * 
     * <br/> Created on 2014-12-1 下午1:30:24

     * @since 4.0
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static boolean isACL() throws KeeperException, InterruptedException{
    	ZooKeeper zk = ZK_SESSION_MANAGER.getZooKeeper();
    	byte[]data = new byte[1];
    	data[0] = 0 ;
    	zk.setData(ZkUtils.PROJECT_PREFIX+"/cluster", data, -1);
    	return true ;
    }
    
    /**
     * create 带有连接监听器的ZkSessionManager
     * 
     * <br/> Created on 2013-12-2 下午1:46:26

     * @since 3.2 
     * @param listener，监听器对象
     * @return
     */
    public static ZkSessionManager createListenerZkSessionManager(ConnectionListener ...listener){
    	ZkSessionManager manager = init();
    	for(int i=0;i<listener.length;i++){
    		manager.addConnectionListener(listener[i]);
    	}
    	return manager;
    }

    /**
	 * 获取分布式锁
	 * <br/> Created on 2013-8-18 上午10:47:03

	 * @since 3.2 
	 * @param serviceName,需要加锁的一个名称，不用的服务名称是不一样的，保证所有加锁的操作唯一

	 * @return
	 */
	public static Lock getDistributedLock(String serviceName){
		if(ZK_SESSION_MANAGER == null){
			throw new RuntimeException("没有加载zk配置，请检查配置文件！");
		}
		String path = BASE_DIR_LOCK+serviceName;
		try {
			Lock lock = LOCK_MAP.get(path);
			if(lock == null){
				lock = new ReentrantZkLock(path, ZK_SESSION_MANAGER);
				LOCK_MAP.put(path, lock);
			}
			return lock;
		} catch (Exception e) {
			
			LOG.error("获取分布式锁异常！", e);
			throw new RuntimeException("获取分布式锁异常！", e);
		} 
		
	}
	/**
	 * 获取动态分布式锁
	 * 此锁会动态创建../serveceName 的节点，当此节点下的子节点都获取锁并执行完毕后
	 * 删除此节点
	 * 
	 * <br/> Created on 2013-11-28 下午3:36:25

	 * @since 3.2 
	 * @param serviceName,动态节点名称
	 * @return
	 */
	public static Lock getDynamicPathDistributedLock(String serviceName){
		if(ZK_SESSION_MANAGER == null){
			throw new RuntimeException("没有加载zk配置，请检查配置文件！");
		}
		String path = BASE_DIR_LOCK+serviceName;
		try {
			Lock lock = new DynamicReentrantZkLock(path, ZK_SESSION_MANAGER);
			return lock;
		} catch (Exception e) {
			
			LOG.error("获取动态分布式锁异常！", e);
			throw new RuntimeException("获取动态分布式锁异常！", e);
		} 
		
	}
	
	/**
	 * 如果不存在zk path ，create path
	 * 
	 * <br/> Created on 2013-11-20 下午5:04:50

	 * @since 3.2 
	 * @param path
	 * @return , path 是否存在
	 */
	public static boolean notExitCreate(ZkSessionManager manager,String path){
		
		boolean result = false;
		try{
			Stat stat = manager.getZooKeeper().exists(path, false);
			if(stat == null){
				String[]paths = path.split("/");
				String part = "";
				for(int i =1;i<paths.length;i++){
					part = part+"/"+ paths[i];
					if( manager.getZooKeeper().exists(part, false) == null){
						manager.getZooKeeper().create(part, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
				}
			}
			result = true ;
		}catch(KeeperException.NodeExistsException e){
			//节点存在异常忽略
			LOG.info("创建存在的节点异常,"+e.getMessage());
		}catch (Exception e) {
			result = false ;
			LOG.error("create zk path exception!", e);
			throw new RuntimeException("create zk path exception!", e);
		}
		return result ;
	}
	/**
	 * 确保删除zk节点
	 * 
	 * <br/> Created on 2014-3-26 下午3:05:23

	 * @since 3.3
	 * @param nodeToDelete
	 * @param version
	 * @return
	 * @throws InterruptedException
	 */
	public static boolean ensureDelete(ZkSessionManager manager, String nodeToDelete, int version) throws InterruptedException{
		 try {
			 	manager.getZooKeeper().delete(nodeToDelete,version);
	            return true;
	        } catch (KeeperException ke) {
	            //if the node has already been deleted, don't worry about it
	            if(ke.code()!= KeeperException.Code.NONODE){
	            	LOG.error(ke.getMessage(), ke);
	            	Thread.sleep(THREAD_SLEEP);
	            	return ensureDelete(manager, nodeToDelete, version);
	            }
	            else{
	            	return false;
	            }
	       }catch(Throwable e){
	    	   LOG.error(e.getMessage(), e);
    	      Thread.sleep(THREAD_SLEEP);
       		  return ensureDelete(manager, nodeToDelete, version);
	       }
	}
	
	/**
	 * 确保创建zk节点
	 * 
	 * <br/> Created on 2013-11-28 下午3:31:42

	 * @since 3.2 
	 * @param path
	 * @param data
	 * @param acl
	 * @param createMode
	 */
	public static String ensureCreate(ZkSessionManager manager , String path , byte[] data, List<ACL> acl, CreateMode createMode){
		return ensureCreate(manager, path, data, acl, createMode,1);
	}
	
	private static String ensureCreate(ZkSessionManager manager , String path , byte[] data, List<ACL> acl, CreateMode createMode, int count){
		String returnPath = "";
		try{
			returnPath = manager.getZooKeeper().create(path, data, acl, createMode);
			
		}catch(KeeperException e){
			if(count == DEFAULT_MAX){
				LOG.error("",e);
				throw new RuntimeException(e);
				
			}
			//如果5次都创建失败，此线程sleep10ms
			if(count%DEFAULT_COUNT ==0){
				try {
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException e1) {
					LOG.error("",e1);
				}
			}
			String newPath = path.trim().substring(0, path.lastIndexOf("/")-1);
			notExitCreate(manager, newPath);
			int newCount = count +1 ;
			returnPath = ensureCreate(manager, path, data, acl, createMode,newCount);
		}catch (Exception e) {
			LOG.error("",e);
			throw new RuntimeException(e);
		}
		return returnPath;
		
	}
	
	
	/**
	 * 初始化
	 * Description: 

	 * @return
	 */
	private static  ZkSessionManager init(){
		Properties ps = PropertiesReader.getProperties("zkConfig");
		String servers = ps.getProperty("servers");
		String timeout = ps.getProperty("timeout");
		if(StringUtils.isEmpty(servers) || StringUtils.isEmpty(timeout)){
			throw new RuntimeException("zkConfig.properties 配置错误！");
		}
		ZkSessionManager manager = new DefaultZkSessionManager(servers.trim(),Integer.parseInt(timeout.trim()));
		return manager;
		
	}
	
	private static ZkSessionManager initB(){
		Properties ps = PropertiesReader.getProperties("zkConfig");
		String servers = ps.getProperty("servers_B");
		String timeout = ps.getProperty("timeout");
		if(StringUtils.isEmpty(servers) || StringUtils.isEmpty(timeout)){
			return null ;
		}
		ZkSessionManager manager = null ;
		try{
			 manager = new DefaultZkSessionManager(servers.trim(),Integer.parseInt(timeout.trim()));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return manager;
	}
	
	/**
	 * 初始化 zk 前缀
	 * @return
	 */
	private static String initPrefix(){
		Properties ps = PropertiesReader.getProperties("zkConfig");
		if(ps == null){
			return "/xx" ;
		}
		String prefix = ps.getProperty("prefix", "/xx");
		return prefix;
	}
	
	public static ZkSessionManager getInstance(String servers,String timeout){
		ZkSessionManager manager = zkSessionManagerMap.get(servers);
		if(manager == null){
			synchronized (obj) {
				manager = zkSessionManagerMap.get(servers);
				if(manager != null){
					return manager;
				}
				manager = new DefaultZkSessionManager(servers.trim(),Integer.parseInt(timeout.trim()));
				if(manager != null){
					zkSessionManagerMap.put(servers, manager);
					return manager;
				}
			}
		}
		return manager;
	}
	
	/**
	 * 获取A/B测试的zk 服务调用
	 * @return
	 */
	public static ZkSessionManager getZkSessionManager(){
		
		if(ZkUtils.ZK_SESSION_MANAGER_B != null){
			return ZkUtils.ZK_SESSION_MANAGER_B ;
		}
		
		return ZkUtils.ZK_SESSION_MANAGER ;
	}
}
