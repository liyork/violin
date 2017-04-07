package com.wolf.company.client;

import com.alibaba.fastjson.JSON;
import com.wolf.company.InvokeModel;
import com.wolf.company.RemoteResult;
import org.junit.Test;

/**
 * Description:
 * <br/> Created on 2016/9/11 8:27
 *
 * @author 李超()
 * @since 1.0.0
 */

public class TestNetytController {

    private static final long DEFAULT_CONNECT_TIMEOUT = 15000;
    private static final long DEFAULT_READTIMEOUT = 30000;

    @Test
    public void hello() {
        TcpClient tcpClient = new TcpClient();
        String projectName = "project2";
        tcpClient.setName(projectName);
        tcpClient.setSockerAddress("127.0.0.1");
        tcpClient.setSoTimeout(DEFAULT_READTIMEOUT);
        tcpClient.setConnectTimeoutMillis(DEFAULT_CONNECT_TIMEOUT);

        tcpClient.init();
//		nettyClient.init();
//        service = (RemoteService) HessainProxyFactory.getProxy(RemoteService.class,nettyClient,serviceId);
        String serviceId = "project2.helloController";

        Class[] claszz = new Class[3];
        claszz[0] = String.class;
        claszz[1] = String.class;
        claszz[2] = String.class;

        Object[] objects = new Object[3];
        objects[0] = serviceId;
        objects[1] = null;
        objects[2] = new Object[]{"xxxxx"};

        //todo 一个远程工程就这样直接使用，如果是多个远程工程，需要对每个工程进行tcpclient缓存
        InvokeModel sendInvokeModel = new InvokeModel(null, null, claszz, objects, serviceId, null);
        sendData(tcpClient, serviceId, sendInvokeModel);

        //休息一下，迎接第二个请求
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendData(tcpClient, serviceId, sendInvokeModel);

        //不中断当前主线程
        try {
            Thread.sleep(500000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendData(TcpClient tcpClient, String serviceId, InvokeModel sendInvokeModel) {
        InvokeModel reInvokeModel = (InvokeModel) tcpClient.sendData(sendInvokeModel, serviceId);
        //判断返回结果是否为null
        if(reInvokeModel == null) {
            System.out.println("null...");
        }

        if(reInvokeModel.getResult() instanceof Exception) {
            Exception e = (Exception) reInvokeModel.getResult();
        }

        RemoteResult vo = reInvokeModel.getResultVO();
        System.out.println(JSON.toJSONString(vo));
    }

}
