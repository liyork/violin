package com.wolf.utils.redis;


import com.wolf.utils.redis.threadpool.CommonThreadPool;
import com.wolf.utils.redis.threadpool.IAsynchronousHandler;
import com.wolf.utils.redis.threadpool.ThreadPoolParameterVO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RedisThreadPool {
	
	private static ExecutorService execute = init();
	
    private static ExecutorService init(){
		ThreadPoolParameterVO vo = new ThreadPoolParameterVO();
		vo.setCorePoolSize(5);
		vo.setMaximumPoolSize(200);
		vo.setInitialCapacity(20000);
		vo.setKeepAliveTime(120);
		vo.setThreadName("base-framework-redis-pipeline-threadPool-");
		vo.setDiscard(false);
		return CommonThreadPool.getThreadPool(vo);
	}
    
    public static Future<Object> submit(IAsynchronousHandler handler) {
    	return execute.submit(handler);
    }

}
