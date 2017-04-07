package com.wolf.company.client;


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
 * hessian客户端管道工厂类
 */
public class HessianClientChannelPipelineFactory implements ChannelPipelineFactory {

    protected NettyContainerConfig config;

    public HessianClientChannelPipelineFactory(NettyContainerConfig config) {
        this.config = config;
    }

    /**
     * 获取管道
     */
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();

        /***
         * 获取 ChannelHandlerList 列表信息， 通过 ChannelHandlerList 的size是否为0来判断netty handler的配置是
         *  1.size >  0 : 新式的配置（责任链形式）
         *  2.size == 0 : 以前的配置形式
         */
        List<ChannelHandler> channelHandlerList = null;

        if(config.getClientExecutionHandlerConfig() != null) {
            channelHandlerList = config.getClientExecutionHandlerConfig().getChannelHandlerList();
        }

        /***
         *  channelHandlerList.size() == 0
         *  老式的配置，指定几种特定的handler
         */
        if(channelHandlerList == null || channelHandlerList.size() == 0) {

            if(config.getClientExecutionHandlerConfig() != null) {
                OneToOneEncoder hessianEncoder = config.getClientExecutionHandlerConfig().getEncoder();
                FrameDecoder hessianDecoder = config.getClientExecutionHandlerConfig().getDecoder();
                if(hessianEncoder != null) {
                    pipeline.addLast("encoder", hessianEncoder);
                }
                if(hessianDecoder != null) {
                    pipeline.addLast("decoder", newChannelHandler(hessianDecoder));
                }
            }
            pipeline.addLast("handler", config.getClientExecutionHandlerConfig().getHandler());
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
                    if(channelHandler instanceof SingleChannelHandler) {
                        pipeline.addLast("handler" + i, channelHandler);
                    }
                    //非单例模式，每次都用个新的实例
                    else if(channelHandler instanceof MultipleChannelHandler) {
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
     *
     * <br/> Created on 2013-10-30 下午2:18:29
     *
     * @param handler
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException

     * @since 3.2
     */
    private ChannelHandler newChannelHandler(ChannelHandler handler) throws InstantiationException, IllegalAccessException {

        return handler.getClass().newInstance();

    }
}
