package com.wolf.test.netty.inaction.realtimeweb;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Description:
 * <br/> Created on 9/27/17 11:19 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class SecureChatServerInitializer extends ChatServerInitializer {
    private final SSLContext context;

    public SecureChatServerInitializer(ChannelGroup group, SSLContext
            context) {
        super(group);
        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        SSLEngine engine = context.createSSLEngine();
        engine.setUseClientMode(false);
        ch.pipeline().addFirst(new SslHandler(engine));
    }
}
