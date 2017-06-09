package com.wolf.companytcp.client;

/**
 * netty发送数据回调接口
 */
public interface CallBack {
    /**
     * netty客户端回调Call
     */
    void call(Object value);

    /**
     * 是否发送数据完成
     */
    boolean isDone();

    /**
     * 获得netty远程调用返回的数据
     */
    Object getData();

}
