package com.wolf.test.base;

import org.junit.Test;

import java.io.IOException;
import java.net.*;

/**
 * Description:
 * <br/> Created on 2018/3/5 14:46
 *
 * @author 李超
 * @since 1.0.0
 */
public class InetAddressTest {

    //jvm参数:-Djava.net.preferIPv4Stack=true则快
    @Test
    public void testSocketSlow() throws IOException {
        long start = System.currentTimeMillis();
        Socket socket = new Socket();
        socket.setReuseAddress(true);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        socket.setSoLinger(true, 0);
        //走的是inet6,是底层的native方法通过dns查找超时了，底层默认是5s。
        socket.connect(new InetSocketAddress("xxx.local", 1111), 1000);
        socket.setSoTimeout(1000);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void test() throws UnknownHostException {

        //通过域名获取ip
        String address = InetAddress.getByName("www.baidu.com").getHostAddress();
        System.out.println("address:" + address);

        //通过ip获取域名
        InetAddress[] addresses = InetAddress.getAllByName("8.8.8.8");
        for (int i = 0; i < addresses.length; i++) {
            String hostname = addresses[i].getHostName();
            System.out.println("hostname:" + hostname);
        }
    }
}
