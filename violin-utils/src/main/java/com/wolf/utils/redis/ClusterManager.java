/**
 * Description: ClusterManager.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

import com.wolf.utils.LoadHierarchyProperties;
import com.wolf.utils.PropertiesReader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  集群管理
 * <br/> Created on 2014-7-3 下午4:09:27

 * @since 3.3
 */
public class ClusterManager {
    /*
     * key：groupName
     */
    public static  ConcurrentHashMap<String, RedisCluster> clusterMap = new ConcurrentHashMap<String, RedisCluster>();

    /**
     * 为了兼容除了framework代码，引用此变量的地方
     */
    public static  ConcurrentHashMap<String, List<RedisReadWriteConfig>> redisConfig = new ConcurrentHashMap<String, List<RedisReadWriteConfig>>();

    private static final Logger LOG = LoggerFactory.getLogger(ClusterManager.class);

    private static Random random = new Random();//获取随机数
    public static final String IP_PORT_SPILT = ":";

    //判断是否加载配置中心成功，如果
    public static boolean configFromCfCenter = false;

    /**
     * Description: tomcat启动时加载redis配置
     * Created on 2016-1-13 下午1:38:42
     * @author
     */
    static {
    	
    	checkNeedRunHA();
    	
        //加载配置中心中的redis配置
        try {
            //加载配置中心中的redis配置
            Properties propertiesFile = PropertiesUtils.getProperties("conf_center");
            if(propertiesFile != null) {
                configFromCfCenter = RedisDynamicConfigLoad.initRedisConfigFromCfcenter();
            }
        } catch (Exception e) {
            LOG.error("redis配置在配置中心解析失败，以本地配置文件为准",e);
        }
        //若配置中心没有则从本地配置文件中加载
        if(!configFromCfCenter) {
            List<Object> list = LoadHierarchyProperties.loadHierarchy("redis");
            if(list != null) {
                for(int i =0;i<list.size();i++){
                    RedisConfig config = (RedisConfig)list.get(i);
                    String groupName = config.getGroupName();
                    RedisCluster redisCluseter = clusterMap.get(groupName);
                    if(redisCluseter == null) {
                        redisCluseter = new RedisCluster(config.getHashStrategy());
                        clusterMap.put(groupName, redisCluseter);
                    }
                    RedisReadWriteConfig redisReadWriteConfig = createRedisReadWriteConfig(config,true);
                    redisCluseter.addProxy(redisReadWriteConfig);

                    /**
                     * 设置冗余变量的值，为兼容调用redisConfig服务代码
                     */
                    setRedundancyData(groupName, redisReadWriteConfig);

                }
            }

            Iterator<ConcurrentHashMap.Entry<String, RedisCluster>> ite = clusterMap.entrySet().iterator();
            while(ite.hasNext()){
                ConcurrentHashMap.Entry<String, RedisCluster> entry = ite.next();

                cardingCluster(entry.getValue());

            }

        }
        /**
         * 加载公共redis配置
         */
        PublicRedisDynamicConfigLoad.initRedisConfigFromCfcenter();

    }
    /**
     * 空方法用于触发静态块的执行
     */
    public static void initRedis() {}

    /**
     * 运行中添加REDIS实例
     * @param configs
     */
    public static void addRedis(List<RedisConfig> configs){
        synchronized (clusterMap) {
            ConcurrentHashMap<String, RedisCluster> tem = new ConcurrentHashMap<String, RedisCluster>();

            for (RedisConfig config : configs) {
                String groupName = config.getGroupName();
                RedisCluster redisCluseter = tem.get(groupName);
                if(redisCluseter == null) {
                    redisCluseter = new RedisCluster(config.getHashStrategy());
                    tem.put(groupName, redisCluseter);
                }
                RedisReadWriteConfig redisReadWriteConfig = createRedisReadWriteConfig(config,true);
                redisCluseter.addProxy(redisReadWriteConfig);

                /**
                 * 设置冗余变量的值，为兼容调用redisConfig服务代码
                 */
                setRedundancyData(groupName, redisReadWriteConfig);
            }

            Iterator<ConcurrentHashMap.Entry<String, RedisCluster>> ite = tem.entrySet().iterator();
            while (ite.hasNext()) {
                ConcurrentHashMap.Entry<String, RedisCluster> entry = ite.next();
                cardingCluster(entry.getValue());
            }

            clusterMap.putAll(tem);
        }
    }

    /**
     * 设置冗余变量的值，为兼容调用redisConfig服务代码，不需要排序
     * @param groupName
     * @param redisReadWriteConfig
     */
    public static void setRedundancyData(String groupName, RedisReadWriteConfig redisReadWriteConfig) {
        if(StringUtils.isBlank(groupName) || redisReadWriteConfig == null) {
            return;
        }
        List<RedisReadWriteConfig> redisReadWriteConfigs = redisConfig.get(groupName);
        if(redisReadWriteConfigs == null) {
            redisReadWriteConfigs = new ArrayList<RedisReadWriteConfig>();
            redisConfig.put(groupName,redisReadWriteConfigs);
        }else {
            redisReadWriteConfigs.add(redisReadWriteConfig);
        }
    }

    /**
     *
     * Description:根据hash类型，梳理集群信息
     * Created on 2016-5-23 下午1:32:06
     * @param cluster
     */
    public static void cardingCluster(RedisCluster cluster) {
        if(cluster.getHashStrategy() == 1) {
            cluster.initConsistentHashing();
        }else {
            //组内按照名称排序
            Collections.sort(cluster.getRedisProxyList());
        }
    }
    /**
     * 通过"%"的方式实现分布式
     *
     * <br/> Created on 2014-7-4 上午9:52:27

     * @since 3.3
     * @param namespace
     * @param key
     * @param groupName
     * @return
     */
    public static final RedisPool clusterModelHash(int namespace,String key ,String groupName,String type){
        RedisReadWriteConfig db = getRedisReadWriteConfigByKey(namespace, groupName, key);
        if(type != null && selectDBByPercent(groupName)){
            return getReadRedis(db);
        }
        return db.getWriteDB();
    }

    /**
     * 通过group获取对应redis集群，通过namespace和key获取对应对应小集群
     * @param namespace
     * @param groupName
     * @param key
     * @return
     */
    public static RedisReadWriteConfig getRedisReadWriteConfigByKey(int namespace, String groupName, String key) {
        String newKey = String.valueOf(namespace)+":"+key;
        RedisCluster redisCluster = clusterMap.get(groupName);
        if(redisCluster == null){
            throw new NullPointerException("can't find redis cluster "+groupName);
        }
        RedisProxy redisProxy = null;
        Integer hashCode = Math.abs(newKey.hashCode());
        if(redisCluster.getHashStrategy() == 0) {
            List<RedisProxy> list = redisCluster.getRedisProxyList();
            if(list.size() > 0) {
                int index = hashCode%list.size();
                redisProxy =  list.get(index);
            }
        }else {
            Node node = redisCluster.getConsistentHashing().getPrimary(newKey);
            redisProxy = redisCluster.getRedisProxyMap().get(node.getName());
        }

        if(redisProxy == null){
            throw new NullPointerException("can't find redis proxy ,groupName="+groupName+",key="+newKey);
        }

        int secondKey = hashCode%100;

        RedisReadWriteConfig config = redisProxy.getRedisConfigMap().get(secondKey);
        if(config == null) {
            throw new NullPointerException("can't find redis instance ,groupName="+groupName+",key="+newKey+",slotGroup="+redisProxy.getSlotGroup()+"weight="+secondKey);
        }

        return config;
    }
    /**
     *
     * Description: 根据配置的写库百分比判断是走写库还是读库
     * @Version1.0 2015-9-6 上午10:40:40 by 创建
     * @return boolean (true 读库 false 写库)
     */
    private static final boolean selectDBByPercent(String groupName){
        if(groupName == null){
            return false;
        }
        try {
            Map<String,Integer> map = WriteDBPercentConfig.WRITEDB_PERCENT_CONFIG.get(WriteDBPercentConfig.REDIS_PERCENT_KEY_PERFIX+groupName);
            Integer basicNum = null;
            Integer splitPointNum = null;
            if(map != null && (basicNum = map.get("basicNum")) != null && (splitPointNum = map.get("splitPointNum")) != null){
                Integer randomNum = random.nextInt(basicNum);
                if(randomNum < splitPointNum){
                    return false;
                }else {
                    return true;
                }
            }
        } catch (Exception e) {
            LOG.error("根据配置的写库百分比判断是走写库还是读库异常,将直接走写库进行查询！",e);
        }
        return false;
    }

    public static final RedisPool getReadRedis(RedisReadWriteConfig db){

        List<RedisPool> list = db.getReadPools();
        int count = 1;
        if(list.size() == 0){
            return db.getReadDB();
        }else{

            count = count + list.size();

            long time = db.getPollCount().get();
            int index = (int)(time % count) ;

            //自增
            db.getPollCount().incrementAndGet();

            if(index == 0){

                return db.getReadDB();
            }
            RedisPool r = list.get(index -1);
            //递归判断
            if(r.isDie){
                return getReadRedis(db);
            }

            return r ;

        }

    }

    /**
     * 获取指定组下的指定redis实例
     *
     * <br/> Created on 2014-7-4 上午9:59:18

     * @since 3.3
     * @param groupName
     * @param name
     * @return
     */
    public static final RedisPool clusterModelHash(String groupName,String name,String type){
        RedisCluster redisCluster = clusterMap.get(groupName);
        if(redisCluster == null){
            throw new NullPointerException("没有对应的redis集群   "+groupName);
        }

        for(RedisProxy proxy : redisCluster.getRedisProxyList()) {
            RedisPool redisPool = getRedisPool(proxy.getRedisConfigList(), groupName, name, type);
            if(redisPool != null) {
                return redisPool;
            }
        }

        throw new NullPointerException("没有对应的redis实例  "+groupName+":"+name);

    }

    /**
     * 获取指定组下的指定redis实例
     *
     * <br/> Created on 2014-7-4 上午9:59:18

     * @since 3.3
     * @param groupName
     * @param name
     * @return
     */
    public static final RedisPool clusterModelHash(String groupName,String slotGroup,String name,String type){
        RedisCluster redisCluster = clusterMap.get(groupName);
        if(redisCluster == null){
            throw new NullPointerException("没有对应的redis集群   "+groupName);
        }

        RedisProxy proxy = redisCluster.getRedisProxyMap().get(slotGroup);

        if(proxy == null) {
            throw new NullPointerException("没有对应的redis小组  "+slotGroup);
        }

        RedisPool redisPool = getRedisPool(proxy.getRedisConfigList(), groupName, name, type);
        if(redisPool == null) {
            throw new NullPointerException("没有对应的redis实例  "+groupName+":"+name);
        }
        return redisPool;

    }

    private static RedisPool getRedisPool(List<RedisReadWriteConfig> list, String groupName,String name, String type) {
        for(int i=0;i<list.size();i++){
            RedisReadWriteConfig db = list.get(i);

            if(!db.getName().equals(name)) {
                continue;
            }

            RedisPool pool = null;
            if(type != null && selectDBByPercent(groupName)){
                pool = getReadRedis(db);
            }else{
                pool = db.getWriteDB();
            }

            return pool;
        }
        return null;
    }

    /**
     * 初始化redis资源
     * @param redisConfig
     * @param isUseZk
     * @return
     */
    public static RedisReadWriteConfig createRedisReadWriteConfig(RedisConfig redisConfig, boolean isUseZk){
        RedisPoolConfig config = redisConfig.getPoolConfig();
        String host = config.getHost();
        String[] hosts = host.split(",");
        RedisReadWriteConfig rw = new RedisReadWriteConfig();
        //设置权重
        rw.setWeight(redisConfig.getWeight());
        rw.setSlotGroup(redisConfig.getSlotGroup());

        for(int i=0;i<hosts.length;i++){
            String h = hosts[i].trim();
            RedisPoolConfig configNew = null;
            try {
                configNew = (RedisPoolConfig) config.clone();
            } catch (CloneNotSupportedException e) {
                LOG.error(e.getMessage(), e);
            }
            setIPAndPort(configNew, h);
            RedisPool newPool = new RedisPool(configNew, redisConfig.getName());
            rw.addDB(newPool);
        }

        //todo-my 工程目前看应该没有配置
        //挂多个读redis 实例
        String readHosts = config.getReadHosts();

        if(!StringUtils.isEmpty(readHosts)){
            String[] rs = readHosts.trim().split(",");
            for(String readHost : rs){
                RedisPoolConfig configNew = null;
                try {
                    configNew = (RedisPoolConfig) config.clone();
                } catch (CloneNotSupportedException e) {
                    LOG.error(e.getMessage(), e);
                }
                setIPAndPort(configNew, readHost);
                RedisPool newPool = new RedisPool(configNew, redisConfig.getName());
                rw.addReadDB(newPool);
            }
        }

        try {
            //如果使用zk，则初始化zk线程监控
            if(isUseZk) {
                rw.init(redisConfig.getRwName() , redisConfig.getName());
            } else {
                //如果没使用zk，说明是配置中心，直接在此赋值
                rw.setReadWriteConfig(redisConfig.getRwName() , redisConfig.getName());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rw;
    }

    private static void setIPAndPort(RedisPoolConfig config, String host) {
        config.setHost(host);

        String [] ipPort = host.split(IP_PORT_SPILT);
        config.setIp(ipPort[0].trim());
        if(ipPort.length == 2) {
            String port = ipPort[1].trim();
            if(StringUtils.isNotBlank(port)) {
                try {
                    config.setPort(Integer.parseInt(port));
                } catch (NumberFormatException e) {
                    LOG.error("{}后的端口号格式不正确！",ipPort[0]);
                }
            }else {
                LOG.error("redis配置文件中ip为{}的：后未指定端口号！",ipPort[0]);
            }
        }
    }
    
    /**
     * 
     * Description: 根据groupName获取所有其下的redis结点实例
     * Created on 2016-7-4 下午2:10:34
     * @param groupName
     * @return
     */
    public static List<RedisReadWriteConfig> getRedisReadWriteConfig(String groupName) {
    	List<RedisReadWriteConfig> list = new ArrayList<RedisReadWriteConfig>();
    	
    	RedisCluster redisCluster = ClusterManager.clusterMap.get(groupName);
		if(redisCluster != null) {
			list = redisCluster.getAllRedisReadWriteConfig();
		}
		
		return list;
    }
    /**
     * 
     * Description: 根据groupName判断集群是否存在
     * Created on 2016-9-28 下午4:18:52
     * @author
     * @param groupName
     * @return
     */
    public static boolean clusterExist(String groupName) {
    	if(clusterMap == null) {
    		return false;
    	}
    	return clusterMap.containsKey(groupName);
    }
    
    /**
     * 
     * Description: 判断是否需要启动HA,若需要则启动
     * Created on 2016-10-27 上午10:27:07
     * @author
     */
    private static void  checkNeedRunHA() {
    	
    	  try {
			  Properties pr = PropertiesReader.getProperties("startInit");
			  if(pr == null) {
				  return;
			  }
			  Iterator<Object> ite = pr.keySet().iterator();
			  while (ite.hasNext()) {
			      String clazz = pr.getProperty(ite.next().toString().trim());
			      if("com.xx.framework.nosql.redis.StartRedisReadWrite".equals(clazz)) {
			    	  Class<?> c = Class.forName(clazz.trim());
			          Object obj = c.newInstance();
			          ((StartRedisReadWrite)obj).execute(null);
			          
			          break;
			      }
			  }
		} catch (Exception e) {
			LOG.error("校验是否需要启动HA失败！",e);
		} 
    }

}
