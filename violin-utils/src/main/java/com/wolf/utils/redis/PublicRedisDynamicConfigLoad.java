package com.wolf.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description: 动态加载redis配置
 * All Rights Reserved.
 * Created on 2016-4-11 下午5:32:56
 *
 */
public class PublicRedisDynamicConfigLoad {

    private static final Logger LOG = LoggerFactory.getLogger(PublicRedisDynamicConfigLoad.class);

    private static final String PUBLIC_REDIS_REGISTER_KEY = "public.redis.register";

    private static RedisClientConfig redisClientConfig;


    /**
     * 初始化
     *
     * @return
     */
    public static boolean initRedisConfigFromCfcenter() {

        /**
         * 封装redis client的配置文件
         * 通过PUBLIC_REDIS_REGISTER_KEY取配置中心依赖的redis应用信息
         */
        redisClientConfig = LoadRedisConfigUtil.makeRedisClientConfig(PUBLIC_REDIS_REGISTER_KEY);
        if (redisClientConfig == null || redisClientConfig.getRedisGroupConfigs() == null
                || redisClientConfig.getRedisGroupConfigs().size() == 0) {
            return false;
        }


        /**
         * 通过用户定义的配置中心应用名称去寻找加载当前项目下面是否包含对应应用的redis配置
         */
//        boolean isLoadSuccess = LoadRedisConfigUtil.initLocalRedisConfig(redisClientConfig);
//
//        if (!isLoadSuccess) {
//            throw new BusinessRuntimeException("load public redis fail!");
//        }
//
//        LOG.error("load public redis config successfully from cfcenter");
//
//        if (DefaultConfigCenterManager.getInstance() != null) {
//            DefaultConfigCenterManager.getInstance().addDataChangeListenerList(new DataChangeListener() {
//                @Override
//                public void call(DataSourceTransport dataSourceTransport) {
//                    if (dataSourceTransport == null) {
//                        return;
//                    }
//                    String sourceName = dataSourceTransport.getClientDataSource().getSourceName();
//                    //如果回调函数是关于PUBLIC_REDIS_REGISTER_KEY或RedisDynamicConfigLoad.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX变化，则重新初始化
//                    if (sourceName.equals(PUBLIC_REDIS_REGISTER_KEY) || sourceName.startsWith(LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX)) {
//                        executeRedisConfigCallBack(dataSourceTransport);
//                    }
//                }
//            });
//        }

        return true;
    }

    /**
     * Description: 回调时加载配置
     * Created on 2016-4-12 上午10:54:03
     *
     */
//    private static void executeRedisConfigCallBack(DataSourceTransport dataSourceTransport) {
//
//
//        //修改连接池场景、修改线程模型、扩容场景、服务器故障恢复或者上线场景。
//        if(dataSourceTransport.getTransferTypeEnum() == TransferTypeEnum.TRANSFER_UPDATE) {
//            if(dataSourceTransport.getClientDataSource().getSourceName().equals(PUBLIC_REDIS_REGISTER_KEY)) {
//                //TODO如果是索引信息发生修改，那么肯定是连接池信息和io模式变化
//                RedisClientConfig newRedisClientConfig = LoadRedisConfigUtil.makeRedisClientConfig(PUBLIC_REDIS_REGISTER_KEY);
//                redisClientConfig = ((newRedisClientConfig == null) ? redisClientConfig : newRedisClientConfig);
//            } else if(dataSourceTransport.getClientDataSource().getSourceName().contains(LoadRedisConfigUtil.CACHECLOUD_REDIS_CONFIG_KEY_PREFIX)) {
//                //这种变化一般是连接池信息发生变话或者是主从切换的时候
//                if(redisClientConfig != null) {
//                    LoadRedisConfigUtil.refreshLocalRedisConfig(dataSourceTransport.getClientDataSource().getSourceName(), redisClientConfig, PUBLIC_REDIS_REGISTER_KEY);
//                } else {
//                    LOG.error("redis config call back execute error,because local client config is null,and the cfcenterkey is"+dataSourceTransport.getClientDataSource().getSourceName());
//                }
//            }
//
//        }
//
//    }

}
