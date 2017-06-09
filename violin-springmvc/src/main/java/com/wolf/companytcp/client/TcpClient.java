package com.wolf.companytcp.client;

import com.wolf.companytcp.*;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.CustomNioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 * <br/> Created on 2017/4/5 17:27
 *
 */
public class TcpClient implements NioClient {

    private static final Logger LOG = LoggerFactory.getLogger(TcpClient.class);

    private static final int DEFAULT_PORT = 9000;

    private static final long DEFAULT_WAIT_TIMEOUT = 15000;

    //一个地址两个工程的情况
    private final ConcurrentHashMap<String, List<Channel>> channelMap = new ConcurrentHashMap<String, List<Channel>>();

    private ClientBootstrap bootstrap = null;

    private long connectTimeoutMillis;

    private long soTimeout;

    private String sockerAddress;

    private int port = DEFAULT_PORT;

    private long waitTimeout = DEFAULT_WAIT_TIMEOUT;

    private boolean tcpNoDelay = false;

    private boolean reuseAddress = true;

    private boolean keepAlive = true;

    private String name;

    private Executor bossExecutor = Executors.newCachedThreadPool();

    private Executor workerExecutor = Executors.newCachedThreadPool();

    private int workerThreadCount = Runtime.getRuntime().availableProcessors();

    private ReentrantLock lock = new ReentrantLock();

//    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    public void setName(String name) {
        this.name = name;
    }

    /**
     * 初始化netty客户端
     */
    @Override
    public void init() {
        bootstrap = new ClientBootstrap(new CustomNioClientSocketChannelFactory(bossExecutor, workerExecutor, ClientConfigUtils.getClientConfig(name).getClientWorkerCount()));
        bootstrap.setOption("tcpNoDelay", tcpNoDelay);
        bootstrap.setOption("connectTimeoutMillis", connectTimeoutMillis);
        bootstrap.setOption("soTimeout", soTimeout);
//		bootstrap.setOption("remoteAddress", new InetSocketAddress(sockerAddress,TcpClient.getNettyContainerConfig().getClientExecutionHandlerConfig().getConnectPort()));
        bootstrap.setOption("reuseAddress", reuseAddress);
        bootstrap.setOption("keepAlive", keepAlive);
        bootstrap.setPipelineFactory(new HessianClientChannelPipelineFactory(ClientConfigUtils.getClientConfig(name)));
    }

    /**
     * 获取连接channel
     * random 负载
     */
    @Override
    public Channel getChannel(String serviceId) {

        final String key = RemoteUtils.getProjectNameToServiceId(serviceId);
        Channel channel = randomChannel(key);
        if(channel == null) {
            lock.lock();
            try {
                if(randomChannel(key) == null) {
                    setConnection(key);
                    channel = randomChannel(key);
                } else {
                    channel = randomChannel(key);
                }
            } finally {
                lock.unlock();
            }
        }
        return channel;

    }

    private Channel randomChannel(String key) {
        List<Channel> channels = channelMap.get(key);
        if(channels == null || channels.size() == 0) {
            return null;
        }
        Random rand = new Random();
        int randNum = rand.nextInt(channels.size());
        return channels.get(randNum);
    }

    private void setConnection(final String key) {
        List<Channel> list = channelMap.get(key);
        NettyContainerConfig clientConfig = ClientConfigUtils.getClientConfig(key);
        int channelSize = clientConfig.getClientExecutionHandlerConfig().getChannelSize();
        if(list != null && list.size() == channelSize) {
            return;
        }
        List<Channel> channels = new ArrayList<Channel>();
        channelMap.put(key, channels);

        ChannelFutureListener channelFutureListener = new ChannelFutureListener() {

            public void operationComplete(final ChannelFuture future) {
                LOG.error(future.getChannel() + " has been closed,cause:" + future.getCause());
                final List<Channel> channels = channelMap.remove(key);
                //移除当前channels 的事件，因为本次已经处理了这个事件，避免重复处理
                //还有，如果不remove 下边的close操作会发生循环调用
                if(!CollectionUtils.isEmpty(channels)) {
                    for(Channel oldChannel : channels) {
                        if(oldChannel == null || future.getChannel() == oldChannel) {
                            LOG.error("channel has already closed,channel" + oldChannel);
                            continue;
                        }
                        oldChannel.getCloseFuture().removeListener(this);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            LOG.error("close channel sleep interrupt", e);
                        }
                        oldChannel.close();
                        LOG.error("invoke channel close method,channel:" + oldChannel);
                    }
                }
            }
        };
        for(int i = 0; i < channelSize; i++) {

            //重置每一个channel write 限制
            bootstrap.setOption("writeBufferHighWaterMark", clientConfig.getClientExecutionHandlerConfig().getWriteBufferHighWaterMark());
            bootstrap.setOption("writeBufferLowWaterMark", clientConfig.getClientExecutionHandlerConfig().getWriteBufferLowWaterMark());
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(sockerAddress, clientConfig.getClientExecutionHandlerConfig().getConnectPort()));
            future.awaitUninterruptibly();
            if(future.isSuccess()) {
                Channel channel = future.getChannel();
                channel.getCloseFuture().addListener(channelFutureListener);
                channels.add(channel);
            } else {
                LOG.error("the I/O operation was completed failed ", future.getCause());
                throw new RuntimeException(future.getCause());
            }
        }

    }

    /**
     * 发送数据<
     */
    @Override
    public Object sendData(Object data, String serviceId) {

        Channel channel = getChannel(serviceId);
        //todo 为什么注释掉？
        if(channel == null) {//获取channel失败,重新获取一次
            /*channel = getChannel(serviceId);
            if(channel == null){*/
            LOG.error(serviceId + " get channel is null");
            /*	throw new RuntimeException(serviceId+ " get channel is null");
            }*/
        }
        ChannelPipeline channelPipeline = channel.getPipeline();
        HessianClientHandler clientHandler = channelPipeline.get(HessianClientHandler.class);
        long seqid = clientHandler.getNextSeqId();
        TransferModel transferModel = new TransferModel();
        transferModel.setId(seqid);
        transferModel.setObject(data);
        transferModel.setProjectName("GlobalMessage.getProjectName()");
//        Integer grayValue = (GrayContext.getRequestGV() == null)?0:GrayContext.getRequestGV();
        Integer grayValue = 0;
        transferModel.setGrayValue(grayValue);

        //判断是否返回结果，如无需返回结果不用进行线程等待
        Map<String, ServiceInfo> map = StartLoadRemoteService.getRemoteService();
        //如果没配置启动加载所有服务，默认不返回结果
        if(map == null) {
            writeChannel(channel, transferModel);
            return null;
        }
        ServiceInfo vo = map.get(serviceId);
        if(vo != null && vo.getNeedResult() == 0) {
            writeChannel(channel, transferModel);
            return null;
        }
        //需要返回结果配置
        CallBack blockingCallback = new BlockingCallback();
        clientHandler.registerCallback(seqid, blockingCallback);

        writeChannel(channel, transferModel);
        //不为了同步其他，只为了阻塞
        synchronized(blockingCallback) {
            boolean isFirst = true;
            while(!blockingCallback.isDone() && isFirst) {
                try {
                    isFirst = false;
                    //工程中默认没有配置
                    int tempWaitTime = RemoteContext.getContext().getWaitTimeout();
                    if(tempWaitTime <= 0 && vo != null) {
                        //使用配置中心的配置
                        long readTimeOut = vo.getReadTimeOut();
                        tempWaitTime = readTimeOut > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) readTimeOut;
                    }
                    if(tempWaitTime > 0) {
                        waitTimeout = tempWaitTime;
                    }
                    blockingCallback.wait(waitTimeout);
                } catch (InterruptedException e) {
                    //工程中一般不会有人直接中断当前线程，所以这个日志暂时没发现
                    clientHandler.removeCallBack(seqid);
                    LOG.error("执行远程服务超时", e.fillInStackTrace());
                    throw new RuntimeException(e);
                }
            }
            //到这里情况：服务器如果没有往channel中写入(return了，或者执行很长时间)，客户端则超过等待时间就到这里了。
            if(blockingCallback.getData() == null) {
                clientHandler.removeCallBack(seqid);
                LOG.error(serviceId + "  :服务执行超过等待时间       " + waitTimeout + "   ，释放资源");
                throw new RuntimeException("serviceId " + serviceId + ":服务执行超过等待时间       " + waitTimeout + "   ，释放资源 解决方案参考 FAQLink.FAQ_002");
            } else {
                return blockingCallback.getData();
            }
        }

    }

    /**
     * 写channel 数据
     *
     * @param channel
     * @param model
     */
    private void writeChannel(Channel channel, TransferModel model) {

        if(channel.isWritable()) {
            channel.write(model);
        } else {
            LOG.error("channel state is " + channel.isOpen());
            throw new RuntimeException("channel buffer full,model" + model.toString());
        }

    }

    public void shutdown() throws InterruptedException {
        bootstrap.releaseExternalResources();
    }

    public long getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(long connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public long getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(long soTimeout) {
        this.soTimeout = soTimeout;
    }

    public String getSockerAddress() {
        return sockerAddress;
    }

    public void setSockerAddress(String sockerAddress) {
        this.sockerAddress = sockerAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Executor getBossExecutor() {
        return bossExecutor;
    }

    public void setBossExecutor(Executor bossExecutor) {
        this.bossExecutor = bossExecutor;
    }

    public Executor getWorkerExecutor() {
        return workerExecutor;
    }

    public void setWorkerExecutor(Executor workersExecutor) {
        this.workerExecutor = workerExecutor;
    }

    public int getWorkerThreadCount() {
        return workerThreadCount;
    }

    public void setWorkerThreadCount(int workerThreadCount) {
        this.workerThreadCount = workerThreadCount;
    }

    public long getWaitTimeout() {
        return waitTimeout;
    }

    public void setWaitTimeout(long waitTimeout) {
        this.waitTimeout = waitTimeout;
    }
}

