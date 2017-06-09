package com.wolf.utils.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从配置中心加载写库百分比配置
 * 格式：
 *  key:projectName.redis.writeDBPercent
 *  value:groupName:basicNum,splitPointNum;groupName:basicNum,splitPointNum
 * @author kongzeng
 *
 */
public class WriteDBPercentConfig {

    private static final Logger LOG = LoggerFactory.getLogger(WriteDBPercentConfig.class);
    
	public static volatile  ConcurrentHashMap<String, Map<String,Integer>> WRITEDB_PERCENT_CONFIG = new ConcurrentHashMap<String, Map<String,Integer>>();
	
//	private static final String KEY = GlobalMessage.getProjectNameNew()+".redis.writeDBPercent";
	
    public static final String REDIS_PERCENT_KEY_PERFIX = "redis:";
    
    static {
    	//todo-my 先注释掉
//		 ConfigCenterUtil.addDataChangeListenerList(new DataChangeListener() {
//             @Override
//             public void call(DataSourceTransport dataSourceTransport) {
//                 if (dataSourceTransport == null || dataSourceTransport.getClientDataSource() == null
//                		 || !KEY.equals(dataSourceTransport.getClientDataSource().getSourceName())) {
//                     return;
//                 }
//
//
//                 init();
//
//             }
//         });
    }
	
	//启动时初始化配置信息
	public synchronized static void init() {
		//todo-my 先注释掉
//		ConfCenterApi confCenterApi = ConfigCenterUtil.getConfCenterApi();
//		if(confCenterApi == null) {
//			return;
//		}
//
		ConcurrentHashMap<String, Map<String,Integer>> tem = new ConcurrentHashMap<String, Map<String,Integer>>();
//
//		ClientDataSource clientDataSource = confCenterApi.getDataSourceByKey(KEY);
//
//	    if(clientDataSource == null) {
//	    	WRITEDB_PERCENT_CONFIG = tem;
//	    	return ;
//	    }
//
//	    String value = clientDataSource.getSourceValue();

		String value = "violinGroup1:10,4;violinGroup2:10,2";
	  
	    if(StringUtils.isEmpty(value)) {
	    	WRITEDB_PERCENT_CONFIG = tem;
	    	return;
	    }
	    
	    LOG.error("load WriteDBPercentConfig from cfcenter :{}", value);
	    
	    String [] groups = value.split(";");
	    
	    for(String group : groups) {
	    	String [] conf  = group.split(":");
	    	if(conf.length != 2) {
	    		LOG.error("config is error:{}", group);
	    		continue;
	    	}
	    	
	    	String groupName = conf[0];
	    	
	    	String [] percent = conf[1].split(",");
	    	
	    	if(percent.length != 2) {
	    		LOG.error("percent is error:{}", group);
	    		continue;
	    	}
	    	
	    	try {
				Map<String,Integer> map = new HashMap<String, Integer>();
				map.put("basicNum", Integer.parseInt(percent[0]));
				map.put("splitPointNum", Integer.parseInt(percent[1]));
				tem.put(REDIS_PERCENT_KEY_PERFIX+groupName, map);
			} catch (Exception e) {
				LOG.error("percent type is error:{}", group);
	    		continue;
			}
	    	
	    }
	    
    	WRITEDB_PERCENT_CONFIG = tem;
		
	}
	
}
