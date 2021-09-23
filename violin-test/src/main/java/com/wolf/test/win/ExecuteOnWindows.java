package com.wolf.test.win;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;

import static com.xebialabs.overthere.ConnectionOptions.*;
import static com.xebialabs.overthere.OperatingSystemFamily.WINDOWS;
import static com.xebialabs.overthere.cifs.CifsConnectionBuilder.CONNECTION_TYPE;
import static com.xebialabs.overthere.cifs.CifsConnectionType.WINRM_INTERNAL;

/**
 * Description:
 * Created on 2021/9/3 5:48 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class ExecuteOnWindows {
    public static void main(String[] args) {

        ConnectionOptions options = new ConnectionOptions();
        options.set(ADDRESS, "172.17.189.219");
        options.set(USERNAME, "administrator");
        options.set(PASSWORD, "ruijie_123");
        //"172.17.189.214", "administrator", "Passw0rd"
        options.set(OPERATING_SYSTEM, WINDOWS);
        //options.set(CONNECTION_TYPE, TELNET);
        options.set(CONNECTION_TYPE, WINRM_INTERNAL);
        OverthereConnection connection = Overthere.getConnection("cifs", options);

        try {
            //connection.execute(CmdLine.build("type", "\\windows\\system32\\drivers\\etc\\hosts"));
            int dir = connection.execute(CmdLine.build("dir"));
        } finally {
            connection.close();
        }
    }
}
