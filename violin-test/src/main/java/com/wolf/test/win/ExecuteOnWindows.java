package com.wolf.test.win;

import com.xebialabs.overthere.*;

import java.util.concurrent.TimeUnit;

import static com.xebialabs.overthere.ConnectionOptions.*;
import static com.xebialabs.overthere.OperatingSystemFamily.WINDOWS;
import static com.xebialabs.overthere.cifs.CifsConnectionBuilder.CONNECTION_TYPE;
import static com.xebialabs.overthere.cifs.CifsConnectionType.WINRM_INTERNAL;

/**
 * Description: 用exec直接将结果输出到console
 * 底层就是开了俩线程，一个接收错误，一个接收信息，用countdownlatch进行waitfor
 * 针对winrm内容
 * 就是一个http调用，不过用的是soap，而有一个协议是叫ws-manager-protocol
 * Created on 2021/9/3 5:48 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class ExecuteOnWindows {
    public static void main(String[] args) throws InterruptedException {

        ConnectionOptions options = new ConnectionOptions();
        options.set(ADDRESS, "172.17.162.143");
        options.set(USERNAME, "administrator");
        options.set(PASSWORD, "1qa@WS3ed");
        options.set(OPERATING_SYSTEM, WINDOWS);
        //options.set(CONNECTION_TYPE, TELNET);
        options.set(CONNECTION_TYPE, WINRM_INTERNAL);
        OverthereConnection connection = Overthere.getConnection("cifs", options);

        try {
            //connection.execute(CmdLine.build("type", "\\windows\\system32\\drivers\\etc\\hosts"));

            int dir = connection.execute(CmdLine.build("dir"));
            System.out.println("dir==>" + dir);
        } finally {
            connection.close();
        }

        TimeUnit.SECONDS.sleep(50);
    }
}
