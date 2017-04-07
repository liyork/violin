package com.wolf.company.server;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;

import java.util.ArrayList;
import java.util.List;

public class ServerExecutionHandlerConfig {
    public static final int DEFAULT_RECEIVE_SIZE = 4194304;
    private static final int DEFAULT_CORE_POOL_SIZE = 200;
    private static final int DEFAULT_MAX_CHANNEL_MEMORY_SIZE = 1048576;
    private static final int DEFAULT_MAX_TOTAL_MEMORY_SIZE = 1048576;
    private static final int DEFAULT_FIX_RECEIVE_SIZE = 768;

    //业务线程池大小
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    //通道最大值
    private long maxChannelMemorySize = DEFAULT_MAX_CHANNEL_MEMORY_SIZE;
    //传输对象最大值
    private long maxTotalMemorySize = DEFAULT_MAX_TOTAL_MEMORY_SIZE;

    private OneToOneEncoder encoder;

    private FrameDecoder decoder;

    private ExecutionHandler executor = null;
    //是否启动，默认启动
    private boolean isStart = true;
    //server 端 接收数据缓冲区大小,默认 4M
    private int receiveBufferSize = DEFAULT_RECEIVE_SIZE;
    //固定大小bytebuffer ,upd 下使用，tcp 使用自适应大小bytebuffer, if isFix true tcp buffer size
    private int fixReceiveBufferSize = DEFAULT_FIX_RECEIVE_SIZE;
    //接收消息是否使用固定buffer，默认使用动态
    private boolean isFix = false;
    //写消息当前channel 最大缓冲区
    private int writeBufferHighWaterMark = 64 * 1024;
    //写消息当前channel 最小缓冲区
    private int writeBufferLowWaterMark = 32 * 1024;

    private SimpleChannelUpstreamHandler handler = new HessianServerHandler();

    private List<ChannelHandler> channelHandlerList = new ArrayList<ChannelHandler>();

    public int getWriteBufferHighWaterMark() {
        return writeBufferHighWaterMark;
    }

    public void setWriteBufferHighWaterMark(int writeBufferHighWaterMark) {
        this.writeBufferHighWaterMark = writeBufferHighWaterMark;
    }

    public int getWriteBufferLowWaterMark() {
        return writeBufferLowWaterMark;
    }

    public void setWriteBufferLowWaterMark(int writeBufferLowWaterMark) {
        this.writeBufferLowWaterMark = writeBufferLowWaterMark;
    }


    public boolean isFix() {
        return isFix;
    }

    public void setFix(boolean isFix) {
        this.isFix = isFix;
    }

    public int getFixReceiveBufferSize() {
        return fixReceiveBufferSize;
    }

    public void setFixReceiveBufferSize(int fixReceiveBufferSize) {
        this.fixReceiveBufferSize = fixReceiveBufferSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    public ExecutionHandler getExecutor() {
        return executor;
    }

    public SimpleChannelUpstreamHandler getHandler() {
        return handler;
    }

    public void setHandler(SimpleChannelUpstreamHandler handler) {
        this.handler = handler;
    }

    public void setExecutor(ExecutionHandler executor) {
        this.executor = executor;
    }

    /**
     * 调整默认值300
     * <br/> Created on 2016-2-2 下午7:09:13
     *
     * @return

     * @since 4.1
     */
    public int getCorePoolSize() {
        if(this.corePoolSize < 300) {
            return 300;
        }
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * 最大 maxChannelMemorySize 默认10M
     *
     * <br/> Created on 2016-1-21 下午3:08:25
     *
     * @return

     * @since 4.1
     */
    public long getMaxChannelMemorySize() {
        if(this.maxChannelMemorySize < 1048576 * 10) {
            return 1048576 * 10;
        }
        return maxChannelMemorySize;
    }

    public void setMaxChannelMemorySize(long maxChannelMemorySize) {
        this.maxChannelMemorySize = maxChannelMemorySize;
    }

    /**
     * 最大maxTotalMemorySize 默认20M
     *
     * <br/> Created on 2016-1-21 下午3:07:40
     *
     * @return

     * @since 4.1
     */
    public long getMaxTotalMemorySize() {
        if(this.maxTotalMemorySize < 1048576 * 20) {
            return 1048576 * 20;
        }
        return maxTotalMemorySize;
    }

    public void setMaxTotalMemorySize(long maxTotalMemorySize) {
        this.maxTotalMemorySize = maxTotalMemorySize;
    }

    public OneToOneEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(OneToOneEncoder encoder) {
        this.encoder = encoder;
    }

    public FrameDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(FrameDecoder decoder) {
        this.decoder = decoder;
    }

    public List<ChannelHandler> getChannelHandlerList() {
        return channelHandlerList;
    }

    public void setChannelHandlerList(List<ChannelHandler> channelHandlerList) {
        this.channelHandlerList = channelHandlerList;
    }

    public void initExecutionHandler() {
        this.executor = new ExecutionHandler(new ProxyMemoryAwareThreadPoolExecutor(2, getCorePoolSize(), this.getMaxChannelMemorySize(), this.getMaxTotalMemorySize()));
    }


}
