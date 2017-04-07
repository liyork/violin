package com.wolf.company.server;

import com.wolf.company.MultipleChannelHandler;
import com.wolf.company.NettyContainerConfig;
import com.wolf.company.SingleChannelHandler;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import static org.jboss.netty.channel.Channels.pipeline;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.util.List;

/**
 * 服务端通讯管道工厂类
 */
@SuppressWarnings("deprecation")
public class TcpServerPipelineFactory implements ChannelPipelineFactory {

    private String configName = "nettyConfig";

    public TcpServerPipelineFactory() {

    }

    public TcpServerPipelineFactory(String configName) {
        this.configName = configName;
    }

    /**
     * 获取通讯管道
     * 注意：执行ChannelHandler链的整个过程是同步的，如果业务逻辑的耗时较长，会将导致Work线程长时间被占用得不到释放，从而影响了整个服务器的并发处理能力
     * 所以，为了提高并发数，通过ExecutionHandler线程池来异步处理ChannelHandler链（worker线程在经过ExecutionHandler后就结束了，它会被ChannelFactory的worker线程池所回收）
     */
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();

        /***
         * 获取 ChannelHandlerList 列表信息， 通过 ChannelHandlerList 的size是否为0来判断netty handler的配置是
         *  1.size >  0 : 新式的配置（责任链形式）
         *  2.size == 0 : 以前的配置形式
         */
        List<ChannelHandler> channelHandlerList = null;

        NettyContainerConfig nettyConfig = StartNettyServer.getNettyConfig(configName);
        ServerExecutionHandlerConfig serverExecutionHandlerConfig = nettyConfig.getServerExecutionHandlerConfig();
        if(serverExecutionHandlerConfig != null) {
            channelHandlerList = serverExecutionHandlerConfig.getChannelHandlerList();
        }


        /***
         *  channelHandlerList.size() == 0
         *  老式的配置，指定几种特定的handler
         */
        if(channelHandlerList == null || channelHandlerList.size() == 0) {

            if(serverExecutionHandlerConfig != null) {

                OneToOneEncoder hessianEncoder = serverExecutionHandlerConfig.getEncoder();
                if(hessianEncoder != null) {
                    pipeline.addLast("encoder", serverExecutionHandlerConfig.getEncoder());
                }

                FrameDecoder hessianDecoder = serverExecutionHandlerConfig.getDecoder();
                if(hessianDecoder != null) {
                    pipeline.addLast("decoder", newChannelHandler(serverExecutionHandlerConfig.getDecoder()));
                }
                pipeline.addLast("executor", serverExecutionHandlerConfig.getExecutor());
            }
            pipeline.addLast("handler", serverExecutionHandlerConfig.getHandler());

        }
        /***
         *  channelHandlerList.size() > 0
         *  新式的配置，handler的链表
         */
        else {

            for(int i = 0; i < channelHandlerList.size(); i++) {
                ChannelHandler channelHandler = channelHandlerList.get(i);
                if(channelHandler != null) {
                    //单例形式
                    if(channelHandler instanceof SingleChannelHandler || channelHandler instanceof SingleChannelHandler) {
                        pipeline.addLast("handler" + i, channelHandler);
                    }
                    //非单例模式，每次都用个新的实例
                    else if(channelHandler instanceof MultipleChannelHandler || channelHandler instanceof MultipleChannelHandler) {
                        pipeline.addLast("handler" + i, channelHandler.getClass().newInstance());
                    }
                }

            }
        }


        return pipeline;
    }

    /**
     * 实例化配置类对象
     * 此实例对象不能缓存，所以每次调用方法重新实例化
     */
    protected ChannelHandler newChannelHandler(ChannelHandler handler) throws InstantiationException, IllegalAccessException {

        return handler.getClass().newInstance();

    }
}
