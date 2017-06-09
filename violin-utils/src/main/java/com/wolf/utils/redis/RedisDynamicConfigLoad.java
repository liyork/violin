package com.wolf.utils.redis;

import com.wolf.utils.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * Description: 动态加载redis配置
 * All Rights Reserved.
 * Created on 2016-4-11 下午5:32:56
 *
 * @author
 */
public class RedisDynamicConfigLoad {

    private static final Logger LOG = LoggerFactory.getLogger(RedisDynamicConfigLoad.class);

    private static final String REGISTER_KEY_SUFFIX = ".redis.group.register";

    private static final String NORMAL_REDIS_REGISTER_KEY = getProjectName() + REGISTER_KEY_SUFFIX;

    private static RedisClientConfig redisClientConfig;
    //本地redis走配置中心相关配置文件名
    private static final String REDIS_REGISTER_CONFIG = "redisRegisterConfig";
    
    private static final String PROJECT_NAME = "projectName";


    /**
     * 初始化
     *
     * @return
     */
    public  static boolean initRedisConfigFromCfcenter() {

        /**
         * 封装redis client的配置文件
         * 通过项目名称和索引后缀取配置中心依赖的redis应用信息
         */
        redisClientConfig = LoadRedisConfigUtil.makeRedisClientConfig(NORMAL_REDIS_REGISTER_KEY);
        if (redisClientConfig == null || redisClientConfig.getRedisGroupConfigs() == null
                || redisClientConfig.getRedisGroupConfigs().size() == 0) {
            return false;
        }


        /**
         * 通过用户定义的配置中心应用名称去寻找加载当前项目下面是否包含对应应用的redis配置
         */
        boolean isLoadSuccess = LoadRedisConfigUtil.initLocalRedisConfig(redisClientConfig);

        if (!isLoadSuccess) {
            //TODO 前期过度期间这样策略，后期的话直接抛出异常
            return false;
        }
        
        LOG.error("load redis config successfully from cfcenter");

        //todo-my 注册中心改变了，需要回调这里，然后改变pool的配置
        /**
         * 创建回调函数
         */
//        if (ClusterManager.clusterMap != null && ClusterManager.clusterMap.size() > 0) {
//            if (DefaultConfigCenterManager.getInstance() != null) {
//                DefaultConfigCenterManager.getInstance().addDataChangeListenerList(new DataChangeListener() {
//                    @Override
//                    public void call(DataSourceTransport dataSourceTransport) {
//                        if (dataSourceTransport == null) {
//                            return;
//                        }
//                        String sourceName = dataSourceTransport.getClientDataSource().getSourceName();
//                        //如果回调函数是关于REGISTER_KEY_SUFFIX和CACHECLOUD_REDIS_CONFIG_KEY_PREFIX变化，则重新初始化
//                        if (sourceName.endsWith(REGISTER_KEY_SUFFIX) || sourceName.startsWith(LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX)) {
//                            executeRedisConfigCallBack(dataSourceTransport);
//                        }
//                    }
//                });
//            }
//        }

        return true;
    }

    

    /**
    //todo-my 先注释掉
    /**
     * Description: 回调时加载配置
     * Created on 2016-4-12 上午10:54:03
     *
     * @author
     */
//    private static void executeRedisConfigCallBack(DataSourceTransport dataSourceTransport) {
//
//
//        //修改连接池场景、修改线程模型、扩容场景、服务器故障恢复或者上线场景。
//        if(dataSourceTransport.getTransferTypeEnum() == TransferTypeEnum.TRANSFER_UPDATE) {
//            if(dataSourceTransport.getClientDataSource().getSourceName().contains(REGISTER_KEY_SUFFIX)) {
//                //TODO如果是索引信息发生修改，那么肯定是连接池信息和io模式变化
//                RedisClientConfig newRedisClientConfig = LoadRedisConfigUtil.makeRedisClientConfig(NORMAL_REDIS_REGISTER_KEY);
//                redisClientConfig = ((newRedisClientConfig == null) ? redisClientConfig : newRedisClientConfig);
//            } else if(dataSourceTransport.getClientDataSource().getSourceName().contains(LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX)) {
//                //这种变化一般是连接池信息发生变话或者是主从切换的时候
//                if(redisClientConfig != null) {
//                    LoadRedisConfigUtil.refreshLocalRedisConfig(dataSourceTransport.getClientDataSource().getSourceName(),redisClientConfig, NORMAL_REDIS_REGISTER_KEY);
//                } else {
//                    LOG.error("redis config call back execute error,because local client config is null,and the cfcenterkey is"+dataSourceTransport.getClientDataSource().getSourceName());
//                }
//            }
//
//        }
//
//    }
    
    /**
     * 
     * Description: 获取项目名
     * Created on 2016-11-2 下午4:41:31
     * @author
     * @return
     */
    private static String getProjectName() {
    	String projectName = "violin";
    	
    	//若启动后获取不到项目名，则从本地配置文件中获取，此策略一般在非tomcat启动且环境变量失效时使用
    	try {
			if(projectName == null) {
				Properties properties = PropertiesReader.getProperties(REDIS_REGISTER_CONFIG);
				if(properties != null) {
					projectName = properties.getProperty(PROJECT_NAME);
				}
			}
		} catch (Exception e) {
			 LOG.error("从本地配置文件中获取项目名失败", e);
		}
    	
    	return projectName;
    }

}
