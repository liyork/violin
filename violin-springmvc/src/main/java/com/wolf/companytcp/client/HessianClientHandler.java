package com.wolf.companytcp.client;

import com.wolf.companytcp.TransferModel;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * hessian客户端渠道处理类
 */
public class HessianClientHandler extends SimpleChannelUpstreamHandler {


    private static final Logger LOG = LoggerFactory.getLogger(HessianClientHandler.class);

    private static AtomicLong seqNum = new AtomicLong(0);

    private static Map<Long, CallBack> callbackMap = new ConcurrentHashMap<Long, CallBack>();

    /**
     * 获取下个序列号
     */
    public long getNextSeqId() {
        return seqNum.getAndIncrement();
    }

    /**
     * 注册客户端回调接口
     */
    public void registerCallback(long seqId, CallBack callback) {
        if(!callbackMap.containsKey(seqId)) {
            callbackMap.put(seqId, callback);
        }

    }

    /**
     * 客户端消息接收
     */
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        TransferModel transferModel = (TransferModel) e.getMessage();
        long seqid = transferModel.getId();
        CallBack callBack = removeCallBack(seqid);
        if(callBack != null) {
            callBack.call(transferModel.getObject());
        }

    }

    /**
     * 客户端消息接收异常捕获
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        LOG.error("", e.getCause());
        Channel ch = e.getChannel();
        if(ch != null) {
            ch.close();
        }
    }

    /**
     * 客户端接收渠道关闭
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOG.error("client closed : " + e.getChannel());
        Channel ch = e.getChannel();
        if(ch != null) {
            ch.close();
        }
    }

    /**
     * 删除callback 缓存
     *
     * @param seqid
     */
    public CallBack removeCallBack(Long seqid) {
        return callbackMap.remove(seqid);
    }


}
