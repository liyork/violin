package com.wolf.test.netty.inaction.customprotocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * Description:
 * <br/> Created on 9/18/17 8:06 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class EchoServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                    .localAddress(new InetSocketAddress("127.0.0.1", 65535))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.config().setAllocator(PooledByteBufAllocator.DEFAULT);

                            System.out.println("connected...; Client:" + ch.remoteAddress());
                            //用4个字节表示整个包的长度，并且废弃掉这四个字节。
                            ch.pipeline().addLast(new MyLengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4))
                                    .addLast(new EchoServerHandler());//todo 这还有顺序？
                        }
                    });
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() +
                    " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
