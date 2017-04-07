/**
 * Description: NioClientSocketChannelFactory.java
 * All Rights Reserved.
 */
package org.jboss.netty.channel.socket.nio;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.SocketChannel;
import org.jboss.netty.util.internal.ExecutorUtil;

import java.util.concurrent.Executor;

/**
 *  参考NioClientSocketChannelFactory，增加了软引用
 */
public class CustomNioClientSocketChannelFactory implements ClientSocketChannelFactory {

    private final Executor bossExecutor;
    private final Executor workerExecutor;
    private final CustomNioClientSocketPipelineSink sink;

    /**
     * Creates a new instance.  Calling this constructor is same with calling
     * {@link #NioClientSocketChannelFactory(Executor, Executor, int)} with 2 *
     * the number of available processors in the machine.  The number of
     * available processors is obtained by {@link Runtime#availableProcessors()}.
     *
     * @param bossExecutor
     *        the {@link Executor} which will execute the boss thread
     * @param workerExecutor
     *        the {@link Executor} which will execute the I/O worker threads
     */
    public CustomNioClientSocketChannelFactory(Executor bossExecutor, Executor workerExecutor) {
        this(bossExecutor, workerExecutor, SelectorUtil.DEFAULT_IO_THREADS);
    }

    /**
     * Creates a new instance.
     *
     * @param bossExecutor
     *        the {@link Executor} which will execute the boss thread
     * @param workerExecutor
     *        the {@link Executor} which will execute the I/O worker threads
     * @param workerCount
     *        the maximum number of I/O worker threads
     */
    public CustomNioClientSocketChannelFactory(Executor bossExecutor, Executor workerExecutor, int workerCount) {
        if(bossExecutor == null) {
            throw new NullPointerException("bossExecutor");
        }
        if(workerExecutor == null) {
            throw new NullPointerException("workerExecutor");
        }
        if(workerCount <= 0) {
            throw new IllegalArgumentException("workerCount (" + workerCount + ") " + "must be a positive integer.");
        }

        this.bossExecutor = bossExecutor;
        this.workerExecutor = workerExecutor;
        sink = new CustomNioClientSocketPipelineSink(bossExecutor, workerExecutor, workerCount);
    }

    public SocketChannel newChannel(ChannelPipeline pipeline) {
        return new NioClientSocketChannel(this, pipeline, sink, sink.nextWorker());
    }

    public void releaseExternalResources() {
        ExecutorUtil.terminate(bossExecutor, workerExecutor);
    }
}
