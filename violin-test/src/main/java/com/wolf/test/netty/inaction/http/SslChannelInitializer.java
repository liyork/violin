package com.wolf.test.netty.inaction.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Description:SslHandler进行安全处理
 * <br/> Created on 9/24/17 1:08 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SSLContext context;
    private final boolean client;
    private final boolean startTls;

    public SslChannelInitializer(SSLContext context, boolean client,
                                 boolean startTls) {
        this.context = context;
        this.client = client;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(client);
        ch.pipeline().addFirst("ssl",
                new SslHandler(engine, startTls));
    }
}