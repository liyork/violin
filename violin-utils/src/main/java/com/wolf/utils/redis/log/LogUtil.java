package com.wolf.utils.redis.log;

import com.wolf.utils.redis.SwitchConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * <br/> Created on 2015-9-14 下午4:50:03

 * @since 4.1
 */
public class LogUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    private static final int DEFAULT_LOG_QUENE_SIZE = 3000;

    private static final LinkedBlockingQueue<NumberLogVo> LOG_QUENE = new LinkedBlockingQueue<NumberLogVo>(DEFAULT_LOG_QUENE_SIZE);

    private static final String projectName = "xxx";
    
    private static final String DATE_PATTERN = "yyyyMMddHHmm";

    private static volatile Map<String, TempNumberLogVo> cacheMap = new ConcurrentHashMap<String, TempNumberLogVo>();

    static {
        if(SwitchConstant.MONITOR_SWITCH){
            new LogFlushThead().start();
        }
    }
    
    /**
     * 计数器实现类
     * @param count
     * @param takeTimes
     * @param operateType
     */
    public static void recordNumberLog(int count, long takeTimes, String operateType){
    	
    	 if(!SwitchConstant.MONITOR_SWITCH){
             return;
         }
    	
    	if(cacheMap.size() > 3000) {
    		if(SwitchConstant.SERVICE_WATCH_SWITCH) {
    			LOGGER.error("计数器，超过最大个数{3000}");
    		}
    		return ;
    	}
    	
        try {
        	SimpleDateFormat formate = new SimpleDateFormat(DATE_PATTERN);
            String date = formate.format(new Date());

            if(StringUtils.isEmpty(operateType)){
                operateType = "UNKNOW";
            }
            
            TempNumberLogVo newVo = null ;
            TempNumberLogVo vo = cacheMap.get(operateType);
            if(vo != null){
            	String lastDate = vo.getStrDate();
            	if(date.equals(lastDate)){
            		synchronized (operateType) {
						vo.setCount(vo.getCount() + count);
						vo.setTakeTimes(vo.getTakeTimes() + takeTimes);
						return ;
					}
            	}else{
            		synchronized (operateType){
            			newVo = new TempNumberLogVo();
            			newVo.setCount(count);
            			newVo.setTakeTimes(takeTimes);
            			newVo.setStrDate(date);
            			TempNumberLogVo oldVo = cacheMap.put(operateType, newVo);
            			newVo = oldVo ;
            			
            		}
            	}
            }else{
            	synchronized (operateType) {
            		newVo = new TempNumberLogVo();
        			newVo.setCount(count);
        			newVo.setTakeTimes(takeTimes);
        			newVo.setStrDate(date);
        			cacheMap.put(operateType, newVo);
        			return ;
				}
            }
            
            NumberLogVo logVo = new NumberLogVo();
			logVo.setCount(newVo.getCount());
			logVo.setHappenTime(date);
			logVo.setTimes(newVo.getTakeTimes());
			logVo.setType(operateType);
			
			LogUtil.recordNumberLog(logVo);
            
            
        }catch(Exception e){
            LOGGER.error("", e);
        }
    }
    

    /**
     * 记录日志
     * @param numLogVo
     */
    public static void recordNumberLog(NumberLogVo numLogVo){

        if(!SwitchConstant.MONITOR_SWITCH){
            return;
        }

        if(numLogVo == null){
            LOGGER.error("numLogVo can not be null!");
            return ;
        }

        if(StringUtils.isEmpty(numLogVo.getType())){
            LOGGER.error("type can not be null! vo:{}", new NumberLogVoWrap(numLogVo));
            return ;
        }

        if(StringUtils.isEmpty(numLogVo.getProjectName())){
            numLogVo.setProjectName(projectName);
        }

        try {
            if (!LOG_QUENE.offer(numLogVo)) {
                LOGGER.error("numberLogQuene is full! this vo will be abadoned: {}", new NumberLogVoWrap(numLogVo));
            }

        }catch(Exception e){
            LOGGER.error("", e);
        }

    }


    static class LogFlushThead extends Thread{

        @Override
        public void run() {

            while(true){
                try{
                    NumberLogVo numberLogVo = LOG_QUENE.poll();
                    if(numberLogVo == null) {
                        Thread.sleep(5);
                    } else {
                        LOGGER.warn("", new NumberLogVoWrap(numberLogVo));
                    }
                } catch(Exception e){
                    LOGGER.error("", e);
                }

            }
        }

    }

    public static Set<String> getBusiKeySet(){
        return cacheMap.keySet();
    }

}