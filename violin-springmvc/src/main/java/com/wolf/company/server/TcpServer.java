/**
 * Description: NettyServer.java
 * All Rights Reserved.
 *
 */
package com.wolf.company.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * netty服务端
 */
public final class TcpServer {

    private String configName = "nettyConfig";

    private ServerBootstrap bootstrap;

    public TcpServer() {
    }

    public TcpServer(String configName) {
        this.configName = configName;
    }

    /**
     * 启动netty服务端，绑定服务端端口
     */
    public void start() {
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), StartNettyServer.getNettyConfig(configName).getServerWorkerCount()));
        bootstrap.setPipelineFactory(new TcpServerPipelineFactory(configName));
        bootstrap.setOption("child.tcpNoDelay", true);//这里设置tcpNoDelay和keepAlive参数，前面的child前缀必须要加上，用来指明这个参数将被应用到接收到的Channels，而不是设置的ServerSocketChannel.ServerSocketChannel的设置是bootstrap.setOption("reuserAddress",true);
        bootstrap.setOption("child.keepAlive", true);
        //增加backlog 设置
        bootstrap.setOption("backlog", 100000);
        if(StartNettyServer.getNettyConfig(configName).getServerExecutionHandlerConfig() != null) {
            bootstrap.setOption("child.receiveBufferSize", StartNettyServer.getNettyConfig(configName).getServerExecutionHandlerConfig().getReceiveBufferSize());
            //判断 是否使用固定 buffersize
            if(StartNettyServer.getNettyConfig(configName).getServerExecutionHandlerConfig().isFix()) {
                bootstrap.setOption("child.receiveBufferSizePredictorFactory", new FixedReceiveBufferSizePredictorFactory(StartNettyServer.getNettyConfig(configName).getServerExecutionHandlerConfig().getFixReceiveBufferSize()));
            }

            bootstrap.setOption("child.writeBufferHighWaterMark", StartNettyServer.getNettyConfig(configName).getServerExecutionHandlerConfig().getWriteBufferHighWaterMark());

            bootstrap.setOption("child.writeBufferLowWaterMark", StartNettyServer.getNettyConfig(configName).getServerExecutionHandlerConfig().getWriteBufferLowWaterMark());
        }


        bootstrap.bind(new InetSocketAddress(StartNettyServer.getNettyConfig(configName).getServerPort()));
    }

    public void stop() {
        bootstrap.releaseExternalResources();
    }

}
