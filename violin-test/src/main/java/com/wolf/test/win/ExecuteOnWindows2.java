package com.wolf.test.win;

import com.xebialabs.overthere.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static com.xebialabs.overthere.ConnectionOptions.*;
import static com.xebialabs.overthere.OperatingSystemFamily.WINDOWS;
import static com.xebialabs.overthere.cifs.CifsConnectionBuilder.CONNECTION_TYPE;
import static com.xebialabs.overthere.cifs.CifsConnectionType.WINRM_INTERNAL;

/**
 * Description: 调用startProcess自己接收返回信息
 * Created on 2021/9/3 5:48 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class ExecuteOnWindows2 {
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

            OverthereProcess dir = connection.startProcess(CmdLine.build("dir"));

            byte[] buf = new byte[1024];
            new Thread(() -> {
                InputStream stdout = dir.getStdout();
                while (true) {
                    try {
                        int read = stdout.read(buf);
                        if (-1 == read) {
                            break;
                        }
                        String s = new String(buf, 0, read);
                        System.out.println("sssss=>" + s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            dir.waitFor();
        } finally {
            connection.close();
        }

        TimeUnit.SECONDS.sleep(50);
    }
}
