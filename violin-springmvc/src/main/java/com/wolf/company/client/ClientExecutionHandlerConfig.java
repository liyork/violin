package com.wolf.company.client;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.util.ArrayList;
import java.util.List;

public class ClientExecutionHandlerConfig {

    private static final int DEFAULT_CONNECT_PORT = 9000;

    private OneToOneEncoder encoder = null;

    private FrameDecoder decoder = null;

    private int channelSize = 2;

    private int connectPort = DEFAULT_CONNECT_PORT;

    //写消息当前channel 最大缓冲区
    private int writeBufferHighWaterMark = 20 * 1024 * 1024;
    //写消息当前channel 最小缓冲区
    private int writeBufferLowWaterMark = 15 * 1024 * 1024;

    private SimpleChannelUpstreamHandler handler = new HessianClientHandler();

    private List<ChannelHandler> channelHandlerList = new ArrayList<ChannelHandler>();

    private Long clientWaitingTimeout = null;

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

    public int getConnectPort() {
        return connectPort;
    }

    public void setConnectPort(int connectPort) {
        this.connectPort = connectPort;
    }

    public int getChannelSize() {
        return channelSize;
    }

    public void setChannelSize(int channelSize) {
        this.channelSize = channelSize;
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

    public SimpleChannelUpstreamHandler getHandler() {
        return handler;
    }

    public void setHandler(SimpleChannelUpstreamHandler handler) {
        this.handler = handler;
    }

    public List<ChannelHandler> getChannelHandlerList() {
        return channelHandlerList;
    }

    public void setChannelHandlerList(List<ChannelHandler> channelHandlerList) {
        this.channelHandlerList = channelHandlerList;
    }

    public Long getClientWaitingTimeout() {
        return clientWaitingTimeout;
    }

    public void setClientWaitingTimeout(Long clientWaitingTimeout) {
        this.clientWaitingTimeout = clientWaitingTimeout;
    }
}
