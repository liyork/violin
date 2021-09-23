package com.wolf.test.win;

import com.google.common.io.BaseEncoding;
import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.cifs.BaseCifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.util.DefaultAddressPortMapper;
import com.xebialabs.overthere.winrm.soap.KeyValuePair;
import com.xebialabs.overthere.winrm.soap.OptionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 支持中文的WINRM
 *
 * @author liwenheng
 */
public class WinRmClientLocal extends WinRmClient {
    private static final Logger logger = LoggerFactory.getLogger(WinRmClientLocal.class);

    static {
        patchWinRm();
    }

    public WinRmClientLocal(String username, String password, URL targetURL, String unmappedAddress, int unmappedPort, SocketFactory socketFactory) {
        super(username, password, targetURL, unmappedAddress, unmappedPort, socketFactory);
    }

    /**
     * overthere的winrm不支持中文
     * <p>
     * 通过反射指定字符集为中文
     */
    private static void patchWinRm() {
        try {
            OptionSet optionSet = OptionSet.OPEN_SHELL;
            List<KeyValuePair> keyValuePairs = optionSet.getKeyValuePairs();
            KeyValuePair kvp = keyValuePairs.get(1);

            Field field = kvp.getClass().getDeclaredField("value");
            field.setAccessible(true);
            field.set(kvp, "54936");
            logger.info("winrm字符集打补丁完毕：{}={}", kvp.getKey(), kvp.getValue());
        } catch (Exception e) {
            logger.warn("winrm字符集打补丁失败：", e);
        }
    }

    public static void main(String[] args) throws MalformedURLException, NoSuchFieldException, IllegalAccessException {
        ConnectionOptions options = new ConnectionOptions();
        options.set(ConnectionOptions.ADDRESS, "172.17.189.214");
        options.set(ConnectionOptions.USERNAME, "administrator");
        options.set(ConnectionOptions.PASSWORD, "Passw0rd");
        options.set(ConnectionOptions.OPERATING_SYSTEM, OperatingSystemFamily.WINDOWS);
        //options.set(CONNECTION_TYPE, TELNET);
        options.set(BaseCifsConnectionBuilder.CONNECTION_TYPE, CifsConnectionType. WINRM_INTERNAL);
        //OverthereConnection connection = Overthere.getConnection("cifs", options);
        CifsWinrsConnection connection = new CifsWinrsConnection("cifs", options, DefaultAddressPortMapper.INSTANCE);
        Field connection1 = connection.getClass().getSuperclass().getDeclaredField("processConnection");
        connection1.setAccessible(true);

        connection1.set(connection,new WinRmConnection(options, DefaultAddressPortMapper.INSTANCE, null));
        //connection.execute(CmdLine.build("dir"));

        StringBuilder sb = new StringBuilder("powershell -encodedcommand ");
        // 不要用java.util.Base64！性能差
        String encodedScript = BaseEncoding.base64().encode("gwmi Win32_Process|select ProcessId,ParentProcessId,CreationDate, ExecutionState,Status,HandleCount,WorkingSetSize,ProcessName".getBytes(StandardCharsets.UTF_16LE));
        sb.append(encodedScript);

        //connection.execute(CmdLine.build("wmic process get name,executablepath,commandline,processid,parentprocessid,threadcount,workingsetsize/value"));
        connection.execute(CmdLine.build(sb.toString()));

        //WinRmClientLocal winRmClientLocal = new WinRmClientLocal("administrator","ruijie_123",
        //        new URL("http", "172.17.189.219", 5985, "/wsman"),"172.17.189.219",5985,
        //        new DefaultSocketFactory());
        //winRmClientLocal.setWinRmTimeout("5000");
        //winRmClientLocal.setHttpsCertTrustStrategy(WinrmHttpsCertificateTrustStrategy.STRICT);
        //String dir = winRmClientLocal.executeCommand("dir");
        //System.out.println(dir);
        //winRmClientLocal.createShell()
        //String dis = winRmClientLocal.executeCommand("dis");
        //System.out.println(dis);
        //winrm.Session('172.17.189.219', auth=('administrator', 'ruijie_123'))

        //ConnectionOptions options = new ConnectionOptions();
        //options.set(ADDRESS, "172.17.189.219");
        //options.set(USERNAME, "'administrator'");
        //options.set(PASSWORD, "'ruijie_123'");
        //options.set(OPERATING_SYSTEM, WINDOWS);
        //options.set(CONNECTION_TYPE, TELNET);
        //OverthereConnection connection = Overthere.getConnection("cifs", options);
        //
        //try {
        //    int type = connection.execute(CmdLine.build("type", "\\windows\\system32\\drivers\\etc\\hosts"));
        //} finally {
        //    connection.close();
        //}
    }
}
