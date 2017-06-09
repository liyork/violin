package com.wolf.utils.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Description: 动态加载redis配置工具类
 * All Rights Reserved.
 * Created on 2016-4-11 下午5:32:56
 *
 * @author
 */
public class LoadRedisConfigUtil {

    private static final Logger LOG = LoggerFactory.getLogger(LoadRedisConfigUtil.class);
    
    public static final String CACHECLOUD_REDIS_CONFIG_KEY_PREFIX = "cachecloud.redis.";


    /**
     * 转换配置中心的配置文件
     *
     * @return
     */
    public static RedisClientConfig makeRedisClientConfig(String registerKey) {
    	
//        ConfCenterApi confCenterApi = ConfigCenterUtil.getConfCenterApi();
//        if (confCenterApi == null) {
//            return null;
//        }
//        /**
//         * 获取当前项目全部的数据源
//         */
//        Map<String, ClientDataSource> dataSourceMap = confCenterApi.getAllDataSource();
//        if (dataSourceMap == null || dataSourceMap.size() == 0) {
//            return null;
//        }
//
//        //加载项目名称.redis.group.register数据源
//        ClientDataSource clientDataSource = dataSourceMap.get(registerKey);
//        if (clientDataSource != null && clientDataSource.getSourceValue() != null) {
//            try {
//                LOG.info("load {} info:{}",registerKey,clientDataSource.getSourceValue());
//                RedisClientConfig redisClientConfig = JSONObject.parseObject(clientDataSource.getSourceValue(), RedisClientConfig.class);
//                return redisClientConfig;
//            } catch (Exception e) {
//                LOG.error("cfcenter redis config load error,and the key is " + registerKey , e);
//                return null;
//            }
//        } else {
//            LOG.warn("not found redis config in cfcenter,current project key is " + registerKey +",get redis config from local");
//        }


        String json = "{ \"redisGroupConfigs\": [ { \"appName\": \"violinApp1\", \"groupName\": \"violinGroup1\" }, { \"appName\": \"violinApp2\", \"groupName\": \"violinGroup2\" }, { \"appName\": \"violinApp3\", \"groupName\": \"violinGroup3\" } ], \"threadType\": 0 }";
        RedisClientConfig redisClientConfig = JSON.parseObject(json, RedisClientConfig.class);
        return redisClientConfig;
    }
    
    
    /**
     * 通过用户定义的配置中心应用名称去寻找加载当前项目下面是否包含对应应用的redis配置
     * @param redisClientConfig
     */
    public synchronized static boolean initLocalRedisConfig(RedisClientConfig redisClientConfig) {

        ConcurrentHashMap<String, RedisCluster> copyRedisConfig = new ConcurrentHashMap<String, RedisCluster>();
        /**
         * 冗余变量
         */
        ConcurrentHashMap<String, List<RedisReadWriteConfig>> redisConfig = new ConcurrentHashMap<String, List<RedisReadWriteConfig>>();

        for (RedisGroupConfig redisGroupConfig : redisClientConfig.getRedisGroupConfigs()) {
            //todo-my 先注释掉
//            ClientDataSource clientDataSource = DefaultConfigCenterManager.getInstance().getConfCenterApi().getDataSourceByKey(LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX + redisGroupConfig.getAppName());
            //如果没有找到，则说明当前redis
//            if (clientDataSource == null || clientDataSource.getSourceValue() == null) {
//                LOG.error("current project need " + LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX + redisGroupConfig.getAppName() + " datasource in cfcenter");
//                return false;
//            }
            try {
//            	LOG.info("load app:{} info:{}",clientDataSource.getSourceName(),clientDataSource.getSourceValue());
                //todo 从配置中心取，每个app配置不同的redis池
                //每个server信息代表一个槽
                RedisAppConfig violinApp1 = JSONObject.parseObject("{ \"appName\": \"violinApp1\", \"appType\": 1, \"dataStatus\": 0, \"hashStrategy\": 0, \"monitorType\": 0, \"serverConfig\": [{ \"hostInfo\": \"127.0.0.1:6380,127.0.0.1:6379\", \"rwName\": \"redis_HA_xxx_2\", \"status\": 0, \"slotGroup\": \"redis_SlotGroup_1\", \"weight\": \"0-99\" }, { \"hostInfo\": \"127.0.0.1:6380,127.0.0.1:6379\", \"rwName\": \"redis_HA_xxx_3\", \"status\": 0, \"slotGroup\": \"redis_SlotGroup_2\", \"weight\": \"0-99\" } ] }", RedisAppConfig.class);
                RedisAppConfig violinApp2 = JSONObject.parseObject("{ \"appName\": \"violinApp2\", \"appType\": 1, \"dataStatus\": 0, \"hashStrategy\": 0, \"monitorType\": 0, \"serverConfig\": [{ \"hostInfo\": \"127.0.0.1:6380,127.0.0.1:6379\", \"rwName\": \"redis_HA_xxx_2\", \"status\": 0, \"slotGroup\": \"redis_SlotGroup_1\", \"weight\": \"0-99\" }, { \"hostInfo\": \"127.0.0.1:6380,127.0.0.1:6379\", \"rwName\": \"redis_HA_xxx_3\", \"status\": 0, \"slotGroup\": \"redis_SlotGroup_2\", \"weight\": \"0-99\" } ] }", RedisAppConfig.class);
                RedisAppConfig violinApp3 = JSONObject.parseObject("{ \"appName\": \"violinApp3\", \"appType\": 1, \"dataStatus\": 0, \"hashStrategy\": 0, \"monitorType\": 0, \"serverConfig\": [{ \"hostInfo\": \"127.0.0.1:6380,127.0.0.1:6379\", \"rwName\": \"redis_HA_xxx_2\", \"status\": 0, \"slotGroup\": \"redis_SlotGroup_1\", \"weight\": \"0-99\" }, { \"hostInfo\": \"127.0.0.1:6380,127.0.0.1:6379\", \"rwName\": \"redis_HA_xxx_3\", \"status\": 0, \"slotGroup\": \"redis_SlotGroup_2\", \"weight\": \"0-99\" } ] }", RedisAppConfig.class);
                Map<String, RedisAppConfig> redisAppConfigMap = new HashMap<>();
                redisAppConfigMap.put("violinApp1", violinApp1);
                redisAppConfigMap.put("violinApp2", violinApp2);
                redisAppConfigMap.put("violinApp3", violinApp3);

                RedisAppConfig redisAppConfig = redisAppConfigMap.get(redisGroupConfig.getAppName());

                //如果找到了，加载对应配置
                if (redisAppConfig != null && redisAppConfig.getServerConfig() != null
                        && redisAppConfig.getServerConfig().size() != 0) {

                    RedisCluster redisCluster = new RedisCluster(redisAppConfig.getHashStrategy());

                    //循环遍历每个server信息
                    for (RedisServerConfig serverConfig : redisAppConfig.getServerConfig()) {
                        //连接池信息，优先以用户自定义信息为主，如果用户定义连接池为空，则以应用为主
                        RedisConfig config = LoadRedisConfigUtil.packageRedisConfig(redisGroupConfig.getGroupName(), serverConfig, redisGroupConfig.getRedisPoolConfig() != null ? redisGroupConfig.getRedisPoolConfig() : redisAppConfig.getPoolConfig());
                        RedisReadWriteConfig redisReadWriteConfig = ClusterManager.createRedisReadWriteConfig(config,LoadRedisConfigUtil.isUseZk(redisAppConfig));
                        //如果集群是单主模式，那么当此节点不可用时，要将isDie置为true，实现缓存穿透
                        if(redisAppConfig.getAppType() == 0 && serverConfig.getStatus() == -1 && !LoadRedisConfigUtil.testRedis(redisReadWriteConfig)) {
                            redisReadWriteConfig.getWriteDB().isDie = true;
                        } else {
                            //如果设置为die，后面恢复了改变其状态
                            redisReadWriteConfig.getWriteDB().isDie = false;
                        }
                        redisCluster.addProxy(redisReadWriteConfig);

                        /**
                         * 设置冗余变量的值，为兼容调用redisConfig服务代码
                         */
                        List<RedisReadWriteConfig> redisReadWriteConfigs = redisConfig.get(redisGroupConfig.getGroupName());
                        if(redisReadWriteConfigs == null) {
                            redisReadWriteConfigs = new ArrayList<RedisReadWriteConfig>();
                            redisConfig.put(redisGroupConfig.getGroupName(),redisReadWriteConfigs);
                        }
                        if(redisReadWriteConfigs != null) {
                            redisReadWriteConfigs.add(redisReadWriteConfig);
                        }
                    }

                    ClusterManager.cardingCluster(redisCluster);

                    copyRedisConfig.put(redisGroupConfig.getGroupName(), redisCluster);
                } else {
                    LOG.error(LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX + redisGroupConfig.getAppName() + " redis config don't contain server data in cfcenter,please notice cachecloud manager");
                    return false;
                }
            } catch (Exception e) {
                LOG.error(LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX + redisGroupConfig.getAppName() + " redis data parse error in cfcenter,please notice cachecloud manager" , e);
                return false;
            }
        }

        ClusterManager.clusterMap.putAll(copyRedisConfig);
        ClusterManager.redisConfig.putAll(redisConfig);
        
        return true;
    }



    /**
     * 回调后，重新初始化对应资源
     * @param sourceName
     */
    public synchronized static void refreshLocalRedisConfig(String sourceName,RedisClientConfig redisClientConfig, String registerKey) {

//        ClientDataSource clientDataSource = DefaultConfigCenterManager.getInstance().getConfCenterApi().getDataSourceByKey(sourceName);
//        if(clientDataSource == null) {
//            return;
//        }
//
//        try {
//        	LOG.info("{} load app:{} info:{} by callback",registerKey, sourceName, clientDataSource.getSourceValue());
//            //获取当前应用信息
//            RedisAppConfig redisAppConfig = JSONObject.parseObject(clientDataSource.getSourceValue(), RedisAppConfig.class);
//
//            if(redisAppConfig == null || redisAppConfig.getServerConfig() == null || redisAppConfig.getServerConfig().size() == 0) {
//                LOG.error(sourceName + " redis config don't contain server data in cfcenter,please notice cachecloud manager");
//                return;
//            }
//
//            List<RedisGroupConfig> groups = new ArrayList<RedisGroupConfig>();
//            //查找当前应用，如果没有则返回
//            for(RedisGroupConfig redisGroupConfigVar : redisClientConfig.getRedisGroupConfigs()) {
//                if(redisGroupConfigVar.getAppName().equals(redisAppConfig.getAppName())) {
//                	groups.add(redisGroupConfigVar);
//                }
//            }
//
//            if(groups.size() == 0) {
//            	LOG.error("not found the groups which used app:{} from {}", redisAppConfig.getAppName(), registerKey);
//                return;
//            }
//
//            for(RedisGroupConfig redisGroupConfig : groups) {
//
//            	boolean result = updateGroup(clientDataSource, redisAppConfig,redisGroupConfig);
//
//            	if(!result) {
//            		return;
//            	}
//
//            }
//
//            LOG.error("{} callback redis config {} successfully,info:{}",registerKey, sourceName,clientDataSource.getSourceValue());
//        } catch (Exception e) {
//            LOG.error(sourceName+ " redis data parse error in cfcenter,please notice cachecloud manager" , e);
//            return ;
//        }

    }

    //todo-my 先注释掉
    /**
     * 回调更新集群信息
     */
//	private static boolean updateGroup(ClientDataSource clientDataSource, RedisAppConfig redisAppConfig, RedisGroupConfig redisGroupConfig) {
//		//如果找到了，加载对应配置
//		RedisCluster newCluster = new RedisCluster(redisAppConfig.getHashStrategy());
//		//兼容list
//		List<RedisReadWriteConfig> redundancyList = new ArrayList<RedisReadWriteConfig>();
//
//		//获取之前集群信息
//		List<RedisReadWriteConfig> beforeData = new ArrayList<RedisReadWriteConfig>();
//
//		RedisCluster oldCluster = ClusterManager.clusterMap.get(redisGroupConfig.getGroupName());
//		if(oldCluster != null) {
//		    beforeData.addAll(oldCluster.getAllRedisReadWriteConfig());
//		}
//
//		//循环遍历每个server信息
//		for (RedisServerConfig serverConfig : redisAppConfig.getServerConfig()) {
//		    //若无有效ip则不处理
//		    if(StringUtils.isBlank(serverConfig.getHostInfo())) {
//		    	//如果是无效IP，取出来之前的信息
//		    	RedisReadWriteConfig oldRedisReadWriteConfig = getCurrentRedis(beforeData, redisGroupConfig.getGroupName(), serverConfig.getRwName());
//		    	if(oldRedisReadWriteConfig != null) {
//		        	if(redisAppConfig.getAppType() == 0) {
//		        		//如果是无效IP且为单点（多主）模式，将状态设置为die
//		        		RedisPool pool = oldRedisReadWriteConfig.getWriteDB();
//		        		pool.isDie = true;
//		        		newCluster.addProxy(oldRedisReadWriteConfig);
//		        	} else {//为集群（主备）模式,则直接用原来的
//		        		newCluster.addProxy(oldRedisReadWriteConfig);
//		        	}
//		        	/**
//		        	 * 添加冗余变量的值，为兼容调用redisConfig服务代码
//		        	 */
//		        	redundancyList.add(oldRedisReadWriteConfig);
//		        	continue;
//		        }else {
//		        	LOG.error("when callback {}, find host of rwname named {} is null,but can't find oldRedisReadWriteConfig,so don't load the config: {} ",clientDataSource.getSourceName(),serverConfig.getRwName(),clientDataSource.getSourceValue());
//		        	return false;
//		        }
//		    }
//		    //连接池信息，优先以用户自定义信息为主，如果用户定义连接池为空，则以应用为主
//		    RedisConfig config = packageRedisConfig(redisGroupConfig.getGroupName(), serverConfig, redisGroupConfig.getRedisPoolConfig() != null ? redisGroupConfig.getRedisPoolConfig() : redisAppConfig.getPoolConfig());
//		    RedisReadWriteConfig redisReadWriteConfig = ClusterManager.createRedisReadWriteConfig(config,isUseZk(redisAppConfig));
//		    //如果集群是单主模式，那么当此节点不可用时，要将isDie置为true，实现缓存穿透
//		    if(redisAppConfig.getAppType() == 0 &&  serverConfig.getStatus() == -1 && !testRedis(redisReadWriteConfig)) {
//		        redisReadWriteConfig.getWriteDB().isDie = true;
//		    }
//		    newCluster.addProxy(redisReadWriteConfig);
//
//		    /**
//		     * 添加冗余变量的值，为兼容调用redisConfig服务代码
//		     */
//		    redundancyList.add(redisReadWriteConfig);
//		}
//
//
//		//对当前组进行排序
//		ClusterManager.cardingCluster(newCluster);
//		//重新初始化
//		ClusterManager.clusterMap.put(redisGroupConfig.getGroupName(), newCluster);
//		//更新兼容数据
//		ClusterManager.redisConfig.put(redisGroupConfig.getGroupName(), redundancyList);
//
//		//关闭之前链接
//		if(beforeData != null) {
//		    for (RedisReadWriteConfig config : beforeData) {
//		        config.abandonOldRedis();
//		    }
//		}
//
//		return true;
//	}

    public static RedisReadWriteConfig getCurrentRedis(List<RedisReadWriteConfig> listConfig, String groupName, String rwName) {
        if (listConfig != null) {
            for (RedisReadWriteConfig config : listConfig) {
                if (rwName.equals(config.getRwName())) {
                    listConfig.remove(config);
                    return config;
                }
            }
        }
        return null;

    }
    /**
     *
     * Description: 是否使用zk高可用模式
     * Created on 2016-5-19 下午3:30:48
     * @author
     * @param redisAppConfig
     * @return
     */
    public static boolean isUseZk(RedisAppConfig redisAppConfig) {
        if(redisAppConfig.getMonitorType() == 1) {
            return false;
        }
        return true;
    }


    /**
     * 组织对象
     *
     * @param groupName
     * @param serverConfig
     * @param poolConfig
     * @return
     */
    public static RedisConfig packageRedisConfig(String groupName, RedisServerConfig serverConfig, RedisPoolConfig poolConfig) {
        //若从配置中心中未加载到连接池的配置，则走默认
        if (poolConfig == null) {
            poolConfig = new RedisPoolConfig();
        }
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setGroupName(groupName);
        redisConfig.setName(serverConfig.getName());
        redisConfig.setRwName(serverConfig.getRwName());
        poolConfig.setHost(serverConfig.getHostInfo());
        redisConfig.setPoolConfig(poolConfig);
        redisConfig.setWeight(serverConfig.getWeight());
        redisConfig.setSlotGroup(serverConfig.getSlotGroup());
        return redisConfig;
    }


    public static boolean testRedis(RedisReadWriteConfig redisReadWriteConfig) {
        Redis redis = null;
        String host = null;
        Integer port = null;
        try {
            RedisPool redispool = redisReadWriteConfig.getWriteDB();
            RedisPoolConfig config = redispool.getConfig();
            host = config.getIp();
            port = config.getPort();
            redis = new Redis(host, port);
            Boolean result = redis.ping();
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            LOG.error("test redis "+host+":"+port+" fail", e);
        }finally {
            if(redis != null){
                redis.close();
            }
        }
        return false;
    }
    
}
