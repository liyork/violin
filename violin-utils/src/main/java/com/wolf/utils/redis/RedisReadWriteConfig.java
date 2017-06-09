/**
 * Description: RedisReadWriteConfig.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  redis 高可用切换vo
 * <br/> Created on 2014-8-27 下午3:26:37

 * @since 3.4
 */
public class RedisReadWriteConfig implements Comparable<RedisReadWriteConfig>{

    public static final String REDIS_SERVER_PATH = "/violin"+"/redis/rw";


    private static final int SLEEP_TIME = 500;

    private static final Logger LOG = LoggerFactory.getLogger(RedisReadWriteConfig.class);
    static{
//        ZkUtils.notExitCreate(ZkUtils.ZK_SESSION_MANAGER, REDIS_SERVER_PATH);
    }

    //标注此redis结点实例是否废弃
    private volatile boolean abandon = false;

    //轮询计数器
    private  AtomicLong pollCount = new AtomicLong();

    public  AtomicLong getPollCount(){
        return pollCount ;
    }

    //当前写redis
    private volatile RedisPool writeDB;

    private volatile RedisPool readDB;

    private volatile RedisPool monitorRedisPool ;

    //redis_HA_xxx_1 用来从zk上获取ip地址的  127.0.0.1,127.0.0.2
    private volatile String rwName ;

    private volatile String name ;

    private volatile boolean isStart = false ;

    private final List<RedisPool> pools = Collections.synchronizedList(new ArrayList<RedisPool>());

    private final List<RedisPool> readPools = new ArrayList<RedisPool>();
    
    /**
     * slot分组名称
     */
    private String slotGroup;
    
    /**
     * 起始
     */
    private int beginWeight = 0;

    /**
     * 终止
     */
    private int endWeight = 99;


    public List<RedisPool> getPools(){
        return this.pools;
    }

    public List<RedisPool> getReadPools(){

        return this.readPools;
    }

    public synchronized void addReadDB(RedisPool pool){

        readPools.add(pool);

    }

    public String getName(){
        return name ;
    }

    public void addDB(RedisPool pool){
        pools.add(pool);
    }

    public synchronized void init(String rw , String name) throws KeeperException, InterruptedException{

        try{
            this.name = name ;
            if(pools.size() == 1){
                writeDB = pools.get(0);
                readDB = pools.get(0);
                setSlaveof();
                if(!isStart){
                    isStart = true ;
                    monitorRedisPool = writeDB ;
                    new MonitorRedisClass().start();
                }
                return ;
            }

            rwName = rw;
            //todo-my 先注释掉
//            ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
//            byte[] bytes = zk.getData(REDIS_SERVER_PATH+"/"+rwName, watcher, null);
//            if(bytes == null) {
//                return;
//            }
//            String ipString = new String(bytes).trim();
            //临时
            if(name .equals( "")) {
                throw new KeeperException.AuthFailedException();
            }

            String ipString = "127.0.0.1:6380,127.0.0.1:6379";
            String[] ips = ipString.split(",");
            if(ips.length ==2){
                writeDB = getPool(ips[0]);
                readDB = getPool(ips[1]);
            }else if(ips.length == 1){
                writeDB = getPool(ips[0]);
                readDB = getPool(ips[0]);
            }else{
                LOG.error("没有可用的redis实例！");
            }

            setSlaveof();
            //启动监控线程
            if(!isStart){
                isStart = true ;
                new MonitorRedisClass().start();
            }

        }catch (KeeperException e) {
            try {
                LOG.error("load redis zk config exception",e);
                Thread.sleep(SLEEP_TIME);
                init(rw, name);
            } catch (InterruptedException e1) {
                LOG.error("",e1);
            }
        }catch(Exception e){
            LOG.error("load redis zk exception",e);
            throw new RuntimeException("获取子目录节点异常!");
        }


    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RedisReadWriteConfig other = (RedisReadWriteConfig) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public RedisPool getWriteDB() {
        return writeDB;
    }

    public RedisPool getReadDB() {
        return readDB;
    }

    private void setSlaveof(){

        //设置多个读redis 实例
        if(writeDB != null){
            RedisPoolConfig targetrw = writeDB.getConfig();
            for(RedisPool p : readPools){
                RedisPoolConfig sourcerw =  p.getConfig();
                changeSlaveof(sourcerw, targetrw);
            }
        }
    }

    private RedisPool getPool(String wor){
        int port = Protocol.DEFAULT_PORT;
        String [] ipAndProt = wor.trim().split(ClusterManager.IP_PORT_SPILT);
        if(ipAndProt.length == 2) {
        	port = Integer.parseInt(ipAndProt[1]);
        }
        for(int i=0;i<pools.size();i++){
            RedisPool pool = pools.get(i);
            if(ipAndProt[0].equals(pool.getConfig().getIp()) && port == pool.getConfig().getPort()){
                return pool;
            }
        }
        LOG.error("can't find {} from local", wor);
        return null ;
    }

    private Watcher watcher = new Watcher() {
        public void process(WatchedEvent event) {
            if (event.getType() == EventType.NodeDataChanged || event.getState() == Event.KeeperState.Expired){
                try {
                    init(rwName , name);
                } catch (KeeperException e) {
                    LOG.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                }
            }

        }
    };

    @Override
    public int compareTo(RedisReadWriteConfig o) {

        if(this.equals(o)){
            return 0;
        }

        if(o == null || o.getName() == null){
            return 1 ;
        }

        if(this.name == null){
            return -1 ;
        }

        return this.getName().compareTo(o.getName());

    }

    /**
     * 动态设置主从
     * @param sourcerw
     * @param targetrw
     */
    private void changeSlaveof(RedisPoolConfig sourcerw, RedisPoolConfig targetrw){
        Redis redis = null;
        try{
            redis = new Redis(sourcerw.getIp(),sourcerw.getPort());
            redis.slaveof(targetrw.getIp(), targetrw.getPort());
        }catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }finally{
            if(redis != null){
                redis.close();
            }
        }

    }


    class MonitorRedisClass extends Thread{



        MonitorRedisClass(){
            super("");
        }

        @Override
        public void run() {
            Map<RedisPool,Boolean> localMap = new HashMap<RedisPool,Boolean>();//用与只读库恢复设置主从关系
            int loadCount = 0;//判断加载标志位
            while(!abandon){
                try{
                	if(monitorRedisPool != null){
                        boolean result = testRedis(monitorRedisPool.getConfig());
                        if(!result){
                            monitorRedisPool.isDie = true ;
                        }else{
                            monitorRedisPool.isDie = false ;
                        }
                    }


                    for(RedisPool pool : readPools){
                        boolean result = testRedis(pool.getConfig());
                        if(!result){
                            pool.isDie = true ;
                            pool.isRetry = true ;
                        }else{
                            pool.isDie = false ;
                            pool.isRetry = false ;
                            if(localMap.get(pool) != null && localMap.get(pool).booleanValue()){
                                RedisPoolConfig targetrw = writeDB.getConfig();
                                RedisPoolConfig sourcerw = pool.getConfig();
                                changeSlaveof(sourcerw, targetrw);
                            }
                        }
                        localMap.put(pool, Boolean.valueOf(pool.isDie));
                    }

                    //五分钟判断zookeeper节点数据是否变化
                    if(loadCount == 2){
                        moniteorWatcher();
                        loadCount = -1;
                    }
                    loadCount ++;

                    Thread.sleep(30 * 1000);

                }catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }

        }
        /**
         * 读取zookeeper信息，如果和本地的配置不一样，则重新初始化节点配置
         * @return
         */
        private void moniteorWatcher(){
            if(pools.size() == 2){
                try {
                	String masterSlaveIP = "";//本地主备节点ip
                	if(writeDB != null && writeDB.getConfig() != null && readDB != null && readDB.getConfig() != null) {
                		if(writeDB.getConfig().getHost()!= null && writeDB.getConfig().getHost().equals(readDB.getConfig().getHost())) {
                			masterSlaveIP = writeDB.getConfig().getIp()+ClusterManager.IP_PORT_SPILT+writeDB.getConfig().getPort();
                		} else {
                			masterSlaveIP = writeDB.getConfig().getIp()+ClusterManager.IP_PORT_SPILT+writeDB.getConfig().getPort() + ","
                		                    +readDB.getConfig().getIp()+ClusterManager.IP_PORT_SPILT+readDB.getConfig().getPort();
                		}
                	}

                	//todo-my 先注释掉
//                    ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
//                    byte[] bytes = zk.getData(REDIS_SERVER_PATH+"/"+rwName, false, null);
//                    String zkIpWithPort = "";
//                    if(bytes != null){
//                        zkIpWithPort = appendPort(new String(bytes).trim());
//                    }
                    String zkIpWithPort = "127.0.0.1:6380,127.0.0.2:6379";
                    if(!masterSlaveIP.equals(zkIpWithPort)){
                    	LOG.error("local:{} and zk:{} is different, need load from zk again",masterSlaveIP,zkIpWithPort);
                        init(rwName , name);
                    }
                }catch (KeeperException e) {
                    LOG.error("load redis zk-"+rwName+" config exception",e);
                } catch (Exception e) {
                    LOG.error("获取reids主备节点-"+rwName+"异常!",e);
                }
            }
        }
        /**
         * 
         * Description: 若无端口号则自动追加默认端口号
         * Created on 2016-6-1 下午5:27:16
         * @author
         * @param zkIPs
         * @return
         */
		private String appendPort(String zkIPs) {
			String[] ips = zkIPs.split(",");
			String zkIpWithPort = ips[0];
			if(ips[0].indexOf(ClusterManager.IP_PORT_SPILT) < 0 ) {
				zkIpWithPort += ClusterManager.IP_PORT_SPILT + Protocol.DEFAULT_PORT;  
			}
			
			if(ips.length == 2) {
				if(ips[1].indexOf(ClusterManager.IP_PORT_SPILT) < 0 ) {
			    	zkIpWithPort += "," + ips[1] + ClusterManager.IP_PORT_SPILT + Protocol.DEFAULT_PORT;  
			    }else {
			    	zkIpWithPort += "," + ips[1];
			    }
			}
			return zkIpWithPort;
		}

        private boolean testRedis(RedisPoolConfig rw){
            int success = 0;
            Redis redis = null;
            //一次成功即认为当前服务器正长
            try{
                redis = new Redis(rw.getIp(), rw.getPort());
                redis.get("1111111111".getBytes("utf-8"));
                return true ;
            }catch(Exception e){
                LOG.error(rw.getHost()+e.getMessage(), e);
            }finally{
                if(redis != null){
                    redis.close();
                }
            }

            //第一次失败，继续尝试5次，如果3次成功，既认为服务器正常，否则服务器异常，剔除
            for(int i =0 ;i<StartRedisReadWrite.FIVE ;i++){

                try {
                    redis = new Redis(rw.getIp(), rw.getPort());
                    redis.get("1111111111".getBytes("utf-8"));
                    success ++ ;
                    if(success>2){
                        return true;
                    }

                } catch (Exception e) {
                    try {
                        Thread.sleep(StartRedisReadWrite.EXCEPTION_SLEEP_TIME);
                    } catch (Exception e1) {

                        LOG.error(rw.getHost()+e1.getMessage(), e1);
                    }
                    LOG.error(rw.getHost()+e.getMessage(), e);
                }finally{
                    if(redis != null){
                        redis.close();
                    }

                }
            }
            return false;

        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRwName() {
        return rwName;
    }

    public void setRwName(String rwName) {
        this.rwName = rwName;
    }

    /**
     * 设置readWriteDB
     */
    public void setReadWriteConfig(String rwName,String name) {
        this.rwName = rwName;
        this.name = name;
        if(pools != null && pools.size() == 1) {
            //当只有一个节点情况下
            this.writeDB = pools.get(0);
            this.readDB = pools.get(0);
        } else if(pools != null && pools.size() == 2) {
            //目前只支持两个节点
            this.writeDB = pools.get(0);
            this.readDB = pools.get(1);
        }
    }

    /**
     *
     * Description:废弃RedisReadWriteConfig
     * Created on 2016-1-13 下午3:20:59
     */
    public void abandonOldRedis() {
        this.setAbandon(true);//用于停止监控线程
        RedisPool oldWriteDB = this.getWriteDB();
        RedisPool oldReadDB = this.getReadDB();
        if(oldWriteDB != null) {
        	oldWriteDB.setAbandon(true);//用于关闭，不再使用的redis连接
        	oldWriteDB.close();
        }
        if(oldReadDB != null) {
        	oldReadDB.setAbandon(true);
        	oldReadDB.close();
        }

        for(RedisPool readPool : this.getReadPools()) {
            readPool.setAbandon(true);
            readPool.close();
        }
    }

    public void setAbandon(boolean abandon) {
        this.abandon = abandon;
    }

	public int getBeginWeight() {
		return beginWeight;
	}

	public void setBeginWeight(int beginWeight) {
		this.beginWeight = beginWeight;
	}

	public int getEndWeight() {
		return endWeight;
	}

	public void setEndWeight(int endWeight) {
		this.endWeight = endWeight;
	}
	
	public void setWeight(String weight) {
        if(!StringUtils.isBlank(weight)) {
            String[] data = weight.split("-");
            if(data.length == 2) {
                beginWeight = Integer.valueOf(data[0]);
                endWeight = Integer.valueOf(data[1]);
            }
        }
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
