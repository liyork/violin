package com.wolf.test.base;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description:
 * <br/> Created on 2018/3/5 14:46
 *
 * @author 李超
 * @since 1.0.0
 */
public class InetAddressTest {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress[] allByName = InetAddress.getAllByName("www.baidu.com");
        for (InetAddress inetAddress : allByName) {
            System.out.println(inetAddress.getHostAddress());
        }
    }
}
