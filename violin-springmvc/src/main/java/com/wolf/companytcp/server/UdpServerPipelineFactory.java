/**
 * Description: ConnectionlessServerPipelineFactory.java
 * All Rights Reserved.
 *
 */
package com.wolf.companytcp.server;

import com.wolf.companytcp.HessianDecoder;
import com.wolf.companytcp.MultipleChannelHandler;
import com.wolf.companytcp.SingleChannelHandler;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import static org.jboss.netty.channel.Channels.pipeline;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.util.List;

/**
 * UDP channelpipline
 */
public class UdpServerPipelineFactory extends TcpServerPipelineFactory {

    @Override
    public ChannelPipeline getPipeline() throws Exception {

        ChannelPipeline pipeline = pipeline();

        /***
         * 获取 ChannelHandlerList 列表信息， 通过 ChannelHandlerList 的size是否为0来判断netty handler的配置是
         *  1.size >  0 : 新式的配置（责任链形式）
         *  2.size == 0 : 以前的配置形式
         */
        //下面为责任链形式的handler配置，此时不支持上面的配置方式，故将pipline清空
        List<ChannelHandler> channelHandlerList = null;

        if(StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig() != null) {
            channelHandlerList = StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getChannelHandlerList();
        }


        /***
         *  channelHandlerList.size() == 0
         *  老式的配置，指定几种特定的handler
         */
        if(channelHandlerList == null || channelHandlerList.size() == 0) {

            if(StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig() != null) {

                OneToOneEncoder hessianEncoder = StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getEncoder();

                FrameDecoder hessianDecoder = (HessianDecoder) StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getDecoder();

                if(hessianEncoder != null) {

                    pipeline.addLast("encoder", StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getEncoder());
                }
                if(hessianDecoder != null) {

                    pipeline.addLast("decoder", newChannelHandler(StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getDecoder()));
                }
                pipeline.addLast("executor", StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getExecutor());
            }
            pipeline.addLast("handler", StartNettyServer.getNettyConfig().getConnectionlessExecutionHandlerConfig().getHandler());

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


}
