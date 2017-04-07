/**
 * Description: CustomNioClientSocketPipelineSink.java
 * All Rights Reserved.
 */
package org.jboss.netty.channel.socket.nio;

import com.wolf.company.SoftMark;
import org.jboss.netty.channel.*;

import java.lang.ref.SoftReference;
import java.util.concurrent.Executor;

/**
 * 增加软引用判断，在需要的时候可以设置软引用
 * 避免高并发引起系统内存溢出
 */
public class CustomNioClientSocketPipelineSink extends NioClientSocketPipelineSink {

    //最大channel send size,default 100M
    private static final int MAX_BUFFER_SIZE = 104857600;

    public CustomNioClientSocketPipelineSink(Executor bossExecutor, Executor workerExecutor, int workerCount) {
        super(bossExecutor, workerExecutor, workerCount);

    }

    @Override
    public void eventSunk(ChannelPipeline pipeline, ChannelEvent e) throws Exception {
        if(e instanceof ChannelStateEvent) {
            super.eventSunk(pipeline, e);
        } else if(e instanceof MessageEvent) {
            MessageEvent event = (MessageEvent) e;

            Object message = ((MessageEvent) e).getMessage();

            NioSocketChannel channel = (NioSocketChannel) event.getChannel();
            int bufferSize = channel.writeBufferSize.get();
            if(message instanceof SoftMark && bufferSize > MAX_BUFFER_SIZE) {

                event = new SoftDownstreamMessageEvent(event.getChannel(), event.getFuture(), new SoftReference<Object>(((SoftMark) message).getMessage()), ((MessageEvent) e).getRemoteAddress());
            } else if(message instanceof SoftMark) {
                event = new DownstreamMessageEvent(channel, event.getFuture(), ((SoftMark) message).getMessage(), event.getRemoteAddress());
            }

            boolean offered = channel.writeBuffer.offer(event);
            assert offered;
            channel.worker.writeFromUserCode(channel);
        }
    }

}
