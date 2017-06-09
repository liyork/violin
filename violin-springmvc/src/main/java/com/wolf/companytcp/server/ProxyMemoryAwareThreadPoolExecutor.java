/**
 * Description: ProxyMemoryAwareThreadPoolExecutor.java
 * All Rights Reserved.
 *

 */
package com.wolf.companytcp.server;

import org.jboss.netty.handler.execution.MemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.util.concurrent.Executor;

/**
 *  netty 线程池代理类
 *  支持动态切换：1，channel 有序
 *  2，随机，性能高
 */
public class ProxyMemoryAwareThreadPoolExecutor implements Executor {

//	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyMemoryAwareThreadPoolExecutor.class);

    private volatile MemoryAwareThreadPoolExecutor proxyExecutor;

    /**
     *
     * @param type  1:channel 有序线程池 .  2：随机执行线程池，并发性高
     * @param corePoolSize
     * @param maxChannelMemorySize
     * @param maxTotalMemorySize
     */
    public ProxyMemoryAwareThreadPoolExecutor(int type, int corePoolSize, long maxChannelMemorySize, long maxTotalMemorySize) {
        if(type == 1) {
            proxyExecutor = new OrderedMemoryAwareThreadPoolExecutor(corePoolSize, maxChannelMemorySize, maxTotalMemorySize);
        } else {
            proxyExecutor = new MemoryAwareThreadPoolExecutor(corePoolSize, maxChannelMemorySize, maxTotalMemorySize);
        }

    }

    @Override
    public void execute(Runnable command) {

        proxyExecutor.execute(command);

    }

    /**
     * shutdown proxyExecutor
     *
     * <br/> Created on 2016-1-21 下午3:01:40

     * @since 4.1
     */
    public void shutdown() {
        if(proxyExecutor != null) {
            proxyExecutor.shutdown();
        }
    }

    /**
     * 从新加载线程池
     * 支持热加载
     *
     * <br/> Created on 2016-1-21 下午3:01:47

     * @since 4.1
     * @param proxyExecutor
     */
    public void reload(MemoryAwareThreadPoolExecutor proxyExecutor) {
        this.shutdown();
        this.proxyExecutor = proxyExecutor;
    }

}
