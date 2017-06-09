/**
 * Description: SwitchConstant.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;


import com.wolf.utils.PropertiesReader;

/**
 * 控制功能启动的类
 *  
 * <br/> Created on 2013-1-31 下午3:45:36

 * @since 2.0
 */
public final class SwitchConstant {
	
	/**
	 * 数据库读写分离开关
	 */
	public static volatile boolean DB_PROXY = switching("db_proxy");
	/**
	 * 新数据库读写分离开关
	 */
	public static volatile boolean NEW_DB_PROXY = true;

	/**
	 * 阻塞队列负载均衡算法开关
	 */
	public static volatile boolean BLOCK_QUEUE_LB_OPEN = false;


	/**
	 * 阻塞队列负载均衡算法日志统计开关
	 */
	public static volatile boolean BLOCK_QUEUE_LB_LOG_COUNT = false;
	
	/**
	 * 日志监控总开关
	 */
	public static volatile boolean MONITOR_SWITCH = switching("logger_monitor_switch");
	
	/**
	 * 服务调用链日志开关
	 */
	public static volatile boolean SERVICE_LINK_SWITCH = true;
	
	/**
	 * 国际化开关
	 */
	public static volatile  boolean INTERNATIONAL_SWITCH = switching("i18n_switch",true);
	
	/**
	 * 软负载总开关
	 */
	public static volatile  boolean SOFT_BALANCER_SWITCH = switching("soft.loadBalancer.switch");
	
	/**
	 * 启动metaq接收消息开关,默认启动
	 */
	public static volatile  boolean METAQ_RECEIVE_START = switching("metaq.receive_start",true);


    public static volatile boolean SCHEDULER_TIMER_START = switching("scheduler.timer.start", false);
    /**
     * 是否启用session，默认启用
     */
    public static volatile  boolean IS_USED_SESSION = switching("is.used.session",true);
    
    /**
	 * 是否使用zookeeper，默认依赖
	 */
	public static volatile  boolean IS_USED_ZK = switching("is.used.zk",true);
	
	
	/**
	 * 数据库读写分离    ——是否能容忍数据同步 延时，默认不能容忍
	 * DB_PROXY = false  && DB_PROXY_DELAYED=true  忽略 延时生效
	 */
	public static volatile  boolean DB_PROXY_DELAYED = switching("db.proxy.delayed");
	

    /**
     * 是否启用表单防篡改功能，默认关闭
     */
    public static volatile boolean FORM_GUARD_SWITCH =  switching("form.guard",false);

	/**
	 * ABTEST功能是否开启
	 */
	public static volatile boolean ABTEST_CLIENT_SWITCH = switching("abtest.client.switch", false);

    /**
     * log拆分开关
     */
    public static volatile boolean LOG_SPLIT_SWITCH =  false;

	/**
	 * Hbase是否记录日志往HBase里写数据，默认写
	 */
	public static volatile  boolean HBASE_WRITE_INFO = true;


	/**
	 * framework中mq启动时是否加载默认meta_cache的consumer
	 */
	public static volatile  boolean FRAMEWORK_METACACHE_CONSUMER_SWITCH = false;

	/**
	 * 负载均衡是否记录相应日志，默认记录
	 */
	public static volatile  boolean FRAMEWORK_LOAD_BALANCE_WRITE_LOG = true;
	
	/**
	 * 启用依赖carlog的动态配置开关
	 */
	public static volatile boolean DYNAMIC_CONFIG_SWITCH = switching("dynamic.config.switch", true);
	/**
	 * sddl日志开关
	 */
	public static volatile boolean SDDL_LOG_SWITCH = false;
	/**
	 * 服务发现日志记录开关
	 */
	public static volatile boolean GET_SERVER_LOG_SWITCH = switching("get.server.log.switch");;
	/**
	 * 解析sql式读写分离开关
	 */
	public static volatile boolean SQL_PARSE_DB_PROXY_SWITCH = false;
	/**
	 * es双写同步开关
	 */
	public static volatile boolean ES_SYN = false;
	/**
	 * memcache命令执行监控开关
	 */
	public static volatile boolean MEMCACHED_COMMANDS_MONITOR_SWITCH = false;
	
	/**
	 * serviceWatch超出异常开关
	 */
	public static volatile boolean SERVICE_WATCH_SWITCH = true;
	/**
	 * 是否允许redis高可用使用多机器同时检测
	 */
	public static volatile boolean REDIS_HA_CLUSTER_CHECK = false;
	
	/**
	 * 是否打印redis命令执行耗时
	 */
	public static volatile boolean PRINT_REDIS_TIME_LOG = false;

	public static volatile boolean SQL_EXECUTE_TRACER_SWITCH = false;
    
    private SwitchConstant(){}
	/**
	 * 
	 * Description: 控制功能启动类的方法,默认FALSE

	 * @return
	 */
	private static boolean switching(String name){
		
		return switching(name,false);
	}
	
	/**
	 * 控制功能启动类的方法
	 * <br/> Created on 2013-6-3 下午3:50:23
	 * @author  李青原(liqingyuan1986@aliyun.com)
	 * @since 3.0 
	 * @param name			properties文件属性的名称
	 * @param defaultValue  默认值
	 * @return
	 */
	private static boolean switching(String name,boolean defaultValue){
		
		Boolean isSwitch = PropertiesReader.getAppointPropertiesAttribute("switch", name, Boolean.class);
		if(isSwitch == null){
			return defaultValue;
		}else{
			return isSwitch;
		}
	}
}
