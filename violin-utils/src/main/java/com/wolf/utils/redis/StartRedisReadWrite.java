/**
 * Description: StartRedisReadWrite.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

import com.wolf.utils.PropertiesReader;
import com.wolf.utils.redis.log.LogUtil;
import com.wolf.utils.zookeeper.ZkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * 管理redis 高可用、读写分离
 * //todo-my 这段还得以后再仔细研究
 * //todo-my 经了解，admin系统启动会用这个定时检查zookeeper
 * <br/> Created on 2014-8-28 上午11:30:54

 * @since 3.4
 */
public class StartRedisReadWrite implements IStartInit {

	public static final int EXCEPTION_SLEEP_TIME = 200;

	public static final int FIVE = 5;


	public static final String REDIS_RWNAME_LOCK_PATH = "redis/rwNameLock/";

	public static final String REDIS_HA_LOCK = REDIS_RWNAME_LOCK_PATH + "REDIS_MASTER_SLAVE_===";

	private static final Logger LOG = LoggerFactory.getLogger(StartRedisReadWrite.class);


	private static final int THREAD_SLEEP_TIME = 10000;

	private static final String CHECKVALUE = "1111111111";

	private static final long MASTER_SLAVE_DELAY_TIME = 800;//主从同步默认延迟时间

	public static boolean SLAVE_DELAY_CHECK_SWITCH = false;//主从同步延迟校验默认开关

	public  static final int SLAVE_TRY_NUM = 3;//主从配置尝试次数

	public static final int SLAVE_TRY_INTERVAL = 500;//主从配置尝试时间间隔
	/**
	 * 获取redis连接、读超时时间 3秒
	 */
	private static final int TIME_OUT = 3000;

    public static volatile Properties ps = null;
    /**
     * 是否已执行execute方法
     */
    private static volatile boolean isInit = false;

	/* (non-Javadoc)
	 */
	@Override
	public void execute(ServletContextEvent context) {

        if (isInit) {
            return;
        }
        synchronized(StartRedisReadWrite.class) {
            if (isInit) {
                return;
            } else {
                isInit = true;
            }
        }


        //从配置中心加载需要被监控的rw_name
        ps = HighAvailConfig.loadHaConfigFromCenter(true);
        if(ps == null || ps.size() == 0) {
            ps = PropertiesReader.getProperties("redisReaderWriteGroup");
        }

		if(!isSameSegment(ps)){
			return ;
		}
        if(ps != null && ps.size() > 0) {
            ThreadMonitor thread = new ThreadMonitor("redis-HA");
            thread.start();
        }
	}
	/**
	 * 校验主、备是否是相同的ip段
	 * @param ps
	 * @return
	 */
	private boolean isSameSegment(Properties ps){

		Set<Entry<Object, Object>> set = ps.entrySet();
		Iterator<Entry<Object, Object>> ite = set.iterator();
		while(ite.hasNext()){
			Entry<Object, Object> entry = ite.next();
			String value = String.valueOf(entry.getValue()).trim();
			//校验数据是否正确
			if("null".equals(value) || "".equals(value) || value.startsWith("$")){
                ite.remove();
                continue;
			}
			String[]ips = value.split(",");
			if(ips.length != 2){
                ite.remove();
                continue;
			}
			String ip1 = ips[0].substring(0, ips[0].indexOf(".", ips[0].indexOf('.')+1)+1);//127.0.

			String ip2 = ips[1].substring(0, ips[1].indexOf(".", ips[1].indexOf('.')+1)+1);
			if(!ip1.equals(ip2)){
                ite.remove();
                continue;
			}
		}
		return true ;

	}


	//todo-my 这段代码，可能以后需要重写，不过思路在这,就是通过配置中心动态切换主从(可能我想的功能太复杂了，也许就是切换个从。。)
	static class ThreadMonitor extends Thread{


		public ThreadMonitor(String name ){
			super(name);
		}

		@Override
		public void run() {
			Redis redis = new Redis("127.0.0.1");

			while(true){

				try {
					long getInTime = System.currentTimeMillis();

					if(SwitchConstant.REDIS_HA_CLUSTER_CHECK) {//允许多机器同时协同检测

						monitorRedis(redis);

					}else {//同时只有一台机器串行检测所有redis
						//todo-my 可以改成redis的设置原子性保证此功能
						if(hasSet(redis,REDIS_HA_LOCK)){
							monitorRedis(redis);
						}
					}


					long getOutTime = System.currentTimeMillis();
					Long usedMillisecond = getOutTime - getInTime;
					LogUtil.recordNumberLog(1, usedMillisecond, "redis monitor time");

					Thread.sleep(THREAD_SLEEP_TIME);

				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}

			}

		}


		private void  monitorRedis(Redis redis) throws Exception{

			Set<Entry<Object, Object>> set = ps.entrySet();
			Iterator<Entry<Object, Object>> ite = set.iterator();
			while(ite.hasNext()){
				//todo-my 使用redis代替
				String key = null;
				try {
						Entry<Object, Object> entry = ite.next();
						key = String.valueOf(entry.getKey());
						String value = String.valueOf(entry.getValue()).trim();
						//校验数据是否正确
						if("null".equals(value) || "".equals(value) || value.startsWith("$")){
							continue ;
						}
						//增加try catch  至少不会对现有业务有影响
						//增加超时
						try{
							Calendar ca = Calendar.getInstance();
							ca.add(Calendar.MINUTE, 1);

							if(!hasSet(redis,REDIS_RWNAME_LOCK_PATH)){
								continue;
							}

						}catch (Exception e1) {
							LOG.error("", e1);
						}

						ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
						String dataPath = RedisReadWriteConfig.REDIS_SERVER_PATH+"/"+key;
						String[] zkValues = value.split(",");
						if(zk.exists(dataPath, false) == null){
							zk.create(dataPath, value.getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
							tryChangeSlaveofNoOne(zkValues[0], zk , dataPath, false);
							if(zkValues.length == 2){
								tryChangeSlaveof(zkValues[1],zkValues[0], zk , dataPath, true);
							}
						}

						byte[] bytes = zk.getData(dataPath, false, null);
						if(bytes != null && bytes.length>0){
							resetRedis(bytes, zkValues, dataPath,redis);
							//校验主从配置是否正确，捕获异常防止影响现有业务
							try {
								checkMasterAndSlave(zk, dataPath);
							} catch (Exception e) {
								LOG.error("校验"+key+"主从配置异常！",e);
							}
						}else{
							//为null 时，重新设置
							zk.setData(dataPath, value.getBytes("utf-8"), -1);
							tryChangeSlaveofNoOne(zkValues[0], zk , dataPath, false);
							if(zkValues.length == 2){
								tryChangeSlaveof(zkValues[1],zkValues[0], zk , dataPath, true);
							}
						}
					} catch (Exception e) {
						LOG.error(key+String.valueOf(e.getMessage()), e);
					}finally {
					}
			}
		}

		private void resetRedis(byte[] bytes,String[]oldRws,String dataPath,Redis redis) throws Exception{
			ZooKeeper zk = ZkUtils.ZK_SESSION_MANAGER.getZooKeeper();
			String[] rws = new String(bytes,"utf-8").split(",");
			for(int i =0;i<oldRws.length;i++){
				String write = oldRws[i];
				if(testRedis(write)){
					String zkWrite = rws[0];
					if(!write.equals(zkWrite)){
						if(zkWrite == null || zkWrite.equals("")){
							zk.setData(dataPath, write.getBytes("utf-8"),-1);
							tryChangeSlaveofNoOne(write, zk , dataPath, true);
							//todo-my 可能是将cfcenter中的从机器挂载到原来主上
						}else if(rws.length == 1){
							tryChangeSlaveof(write,zkWrite, zk , dataPath, false);
							if(slaveDelayCheck(zkWrite,write)){
								zk.setData(dataPath, (zkWrite+","+write).getBytes("utf-8"), -1);
							}else{
								System.out.println(write+"与主库"+zkWrite+"延迟过大不注册zk");
							}
						}else if(rws.length == 2){//redis修改配置时修改zookeeper节点数据(只有系统重启时用)，yanglei-修改于2015.6.23
							String zkWrite2 = rws[1];
							//todo-my 重新设定zk，slaveof一下
							if(!write.equals(zkWrite2)){
								zk.setData(dataPath, null, -1);
								monitorRedis(redis);
								return;
							}else{
								if(!slaveDelayCheck(zkWrite,zkWrite2)){
									zk.setData(dataPath, zkWrite.getBytes("utf-8"), -1);
									System.out.println(zkWrite+"_"+zkWrite2+"同步延迟过大zk剔除"+zkWrite2);
								}
							}
						}
					}
					//对应文档的2.(2)
				}else{
					if(rws.length == 0){
						LOG.error("没有可用的redis 实例！");
					}else if(rws.length == 1){
						String host = rws[0];
						if(write.equals(host)){
							zk.setData(dataPath, null,-1);
						}
					}else if(rws.length == 2){
						String host = rws[0];
						if(write.equals(host)){
							zk.setData(dataPath, rws[1].getBytes("utf-8"),-1);
							tryChangeSlaveofNoOne(rws[1], zk, dataPath, true);
						}else{
							zk.setData(dataPath, host.getBytes("utf-8"),-1);
//							changeSlaveofNoOne(host);
						}
					}

				}
			}

		}

		private boolean testRedis(String rw){
			int success = 0;
			int fail = 0;
			Redis redis = null;
			//一次成功即认为当前服务器正长
			try{
				redis = getRedisByHost(rw);
				redis.get("1111111111".getBytes("utf-8"));
				return true ;
			}catch(Exception e){
				LOG.warn(rw+String.valueOf(e.getMessage()), e);
			}finally{
				if(redis != null){
					redis.close();
				}
			}

			//第一次失败，继续尝试5次，如果3次成功，既认为服务器正常，否则服务器异常，剔除
			for(int i =0 ;i<FIVE ;i++){

				try {
					redis = getRedisByHost(rw);
					redis.get("1111111111".getBytes("utf-8"));
					success ++ ;
					if(success>2){
						return true;
					}

				} catch (Exception e) {

					LOG.warn(rw+String.valueOf(e.getMessage()), e);

					fail ++ ;
					if(fail > 2) {
						return false;
					}

					try {
						Thread.sleep(EXCEPTION_SLEEP_TIME);
					} catch (Exception e1) {
						LOG.error(e1.getMessage(), e1);
					}

				}finally{
					if(redis != null){
						redis.close();
					}

				}
			}
			return false;

		}

		private void tryChangeSlaveof(String sourcerw, String rw, ZooKeeper zk , String path , boolean updateZk) throws Exception{

			try {
				changeSlaveof(sourcerw, rw);
				return  ;
			} catch (Exception e1) {
			}
			//若设置主从失败，则再进行重试，每次间隔500毫秒，若仍然失败，则将zk结点上的从ip去掉
			for (int i = 0; i < SLAVE_TRY_NUM; i++) {
				try {
					changeSlaveof(sourcerw, rw);
					return;
				}catch (Exception e) {
					if(i == SLAVE_TRY_NUM - 1) {
						if(updateZk){
							zk.setData(path, rw.getBytes("utf-8"), -1);
						}
						throw new Exception(e);
					}else {
						try {
							TimeUnit.MILLISECONDS.sleep(SLAVE_TRY_INTERVAL);
						} catch (Exception e1) {
						}
					}
				}
			}

		}

		private void changeSlaveof(String sourcerw,String rw){
			Redis redis = null;
			try {
				redis = getRedisByHost(sourcerw);
				String[] ipAndPort = rw.split(ClusterManager.IP_PORT_SPILT);
				if (ipAndPort.length == 2 && StringUtils.isNotBlank(ipAndPort[1].trim())) {
					redis.slaveof(ipAndPort[0], Integer.parseInt(ipAndPort[1].trim()));
				} else {
					redis.slaveof(rw, Protocol.DEFAULT_PORT);
				}

				System.out.println("REDIS||" + DateUtils.formatDate(new Date()) + ":" + sourcerw + "设置为" + rw + "的从服务器");
			} finally {
				if (redis != null) {
					redis.close();
				}
			}

		}

		private void tryChangeSlaveofNoOne(String rw, ZooKeeper zk , String path, boolean updateZk) throws Exception{

			try {
				changeSlaveofNoOne(rw);
				return;
			} catch (Exception e1) {
			}

			//若设置主失败，则再进行重试，每次间隔500毫秒，若仍然失败，则将zk结点上的信息清空
			for(int i = 0 ; i < SLAVE_TRY_NUM; i++) {
				try{
					changeSlaveofNoOne(rw);
					return;
				}catch (Exception e) {
					if(i == SLAVE_TRY_NUM - 1) {
						if(updateZk) {
							zk.setData(path, null, -1);
						}
						throw new Exception(e);
					}else {
						try {
							TimeUnit.MILLISECONDS.sleep(SLAVE_TRY_INTERVAL);
						} catch (Exception e1) {
						}
					}
				}
			}

		}

		private void changeSlaveofNoOne(String rw){
			Redis redis = null;
			try{
				redis = getRedisByHost(rw);
				redis.slaveofNoOne();
			}finally{
				if(redis != null){
					redis.close();
				}
			}

		}

    }

	private static boolean hasSet(Redis redis,String key) {
		byte[] keys = new byte[0];
		try {
            keys = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		byte[] bytes = new byte[1];
		bytes[0]= 1;
		return  redis.set(keys, bytes);
	}

	/**
	 * 判断指定时间内主从同步是否完成
	 * @param master
	 * @param slave
	 * @return boolean
	 */
	private static boolean slaveDelayCheck(String master, String slave){
		if(!SLAVE_DELAY_CHECK_SWITCH){
			return true;
		}
		String checkKey = master+"_"+slave+System.currentTimeMillis();
		Redis redisMaster = null;
		Redis redisSlave = null;
		int failure = 0;
		try {
			redisMaster = getRedisByHost(master);
			redisSlave = getRedisByHost(slave);
			redisMaster.setEx(checkKey.getBytes("utf-8"), 300, CHECKVALUE.getBytes("utf-8"));
			Thread.sleep(MASTER_SLAVE_DELAY_TIME);
			byte[] checkvalue = redisSlave.get(checkKey.getBytes("utf-8"));
			if(checkvalue != null && CHECKVALUE.equals(new String (checkvalue,"utf-8" ))){
				return true;
			}
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}finally{
			if( redisMaster != null){
				try {
					redisMaster.del(checkKey.getBytes("utf-8"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				redisMaster.close();
			}
			if(redisSlave != null){
				redisSlave.close();
			}
		}

		//第一次失败，继续尝试5次，如果3次失败，既认为服务器同步超时，剔除，否则服务器正常
		for(int i =0 ;i<FIVE ;i++){

			try {
				redisMaster = getRedisByHost(master);
				redisSlave = getRedisByHost(slave);
				redisMaster.setEx(checkKey.getBytes("utf-8"), 300, CHECKVALUE.getBytes("utf-8"));
				Thread.sleep(MASTER_SLAVE_DELAY_TIME);
				byte[] checkvalue = redisSlave.get(checkKey.getBytes("utf-8"));
				if(checkvalue == null || (!CHECKVALUE.equals(new String (checkvalue,"utf-8" )))){
					failure ++;
				}
				if(failure > 2){
					return false;
				}
			}catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}finally{
				if( redisMaster != null){
					try {
						redisMaster.del(checkKey.getBytes("utf-8"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					redisMaster.close();
				}
				if(redisSlave != null){
					redisSlave.close();
				}
			}
		}
		return true;
	}


	private static Redis getRedisByHost(String host) {
		String [] ipAndPort = host.split(ClusterManager.IP_PORT_SPILT);
		if(ipAndPort.length == 2 && StringUtils.isNotBlank(ipAndPort[1].trim())) {
			try {
				return new Redis(ipAndPort[0].trim(),Integer.parseInt( ipAndPort[1].trim()), TIME_OUT);
			} catch (NumberFormatException e) {
				throw new NumberFormatException(ipAndPort[0]+"后的端口号端口号格式不正确！");
			}
		} else {
			return new Redis(ipAndPort[0],Redis.DEFAULT_PORT, TIME_OUT);
		}
	}
	/**
	 *
	 * Description: 校验主从配置是否正确
	 * Created on 2015-12-8 下午1:47:48
	 * @author
	 * @throws InterruptedException
	 * @throws Exception
	 */
	private static void checkMasterAndSlave(ZooKeeper zk , String path) throws Exception{
		byte[] bytes = zk.getData(path, false, null);
		if(bytes == null || bytes.length == 0) {
			return;
		}
		String[] rws = new String(bytes,"utf-8").split(",");
		if(rws == null || rws.length == 0) {
			return;
		}
		//校验zk结点上的第一个ip是否为主，若不是则直接清空zk结点上的信息
		Redis master = null;
		try {
			master = getRedisByHost(rws[0]);
			List<String> list = master.getConfig("slaveof");
			if(list != null && list.size() == 2) {
				String masterIp = list.get(1);
				if(!"".equals(masterIp)) {
					zk.setData(path, null, -1);
					LOG.error("redis主库："+rws[0]+"主从配置不对！，故将结点信息清空");
					return ;
				}
			}
		} finally {
			if(master != null) {
				master.close();
			}
		}
		//若有从，则校验从服务器的主从配置是否正确，若不正确，则将从库的ip下线
		if(rws.length == 2) {
			Redis slave = null;
			try {
				slave = getRedisByHost(rws[1]);
				List<String> list1 = slave.getConfig("slaveof");
				if(list1 != null && list1.size() == 2) {
					String masterIp = list1.get(1).replace(" ", ":");
					if(!masterIp.startsWith(rws[0])) {
						zk.setData(path, rws[0].getBytes("utf-8"), -1);
						LOG.error("redis从库："+rws[1]+"主从配置不对！，故将从库下线");
					}
				}
			}finally {
				if(slave != null) {
					slave.close();
				}
			}
		}

	}

}
