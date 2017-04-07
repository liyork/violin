/**
 * Description: NettyServer.java
 * All Rights Reserved.
 *
 */
package com.wolf.company.server;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * netty udp server端启动
 */
public final class UdpServer {
    private static UdpServer server = new UdpServer();

    private ConnectionlessBootstrap bootstrap;

    private UdpServer() {
    }

    public static UdpServer getInstance() {
        return server;
    }

    /**
     * 启动方法
     */
    public void start(int port) {
        bootstrap = new ConnectionlessBootstrap(new NioDatagramChannelFactory(Executors.newCachedThreadPool(), StartNettyServer.getNettyConfig().getConnectionlessWorkerCount()));
        bootstrap.setPipelineFactory(new UdpServerPipelineFactory());
        if(StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig() != null) {
            bootstrap.setOption("receiveBufferSize", StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getReceiveBufferSize());
            //设置bytebuffer 大小
            bootstrap.setOption("receiveBufferSizePredictorFactory", new FixedReceiveBufferSizePredictorFactory(StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getFixReceiveBufferSize()));
        }

        bootstrap.bind(new InetSocketAddress(port));
    }

    public void stop() {
        bootstrap.releaseExternalResources();
    }

}
