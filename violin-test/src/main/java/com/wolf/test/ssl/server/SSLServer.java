package com.wolf.test.ssl.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Description:
 * Created on 2021/3/25 1:40 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SSLServer {
    private static final int DEFAULT_PORT = 7777;
    private static final String SERVER_KEY_STORE_PASSWORD = "123456";
    private static final String SERVER_TRUST_KEY_STORE_PASSWORD = "123456";
    private SSLServerSocket serverSocket;

    /**
     * 启动程序
     *
     * @param args
     */
    public static void main(String[] args) {
        SSLServer server = new SSLServer();
        server.init();
        server.start();
    }

    /**
     * <ul>
     * <li>听SSL Server Socket</li>
     * <li> 由于该程序不是演示Socket监听，所以简单采用单线程形式，并且仅仅接受客户端的消息，并且返回客户端指定消息</li>
     * </ul>
     */
    public void start() {
        if (serverSocket == null) {
            System.out.println("ERROR");
            return;
        }
        while (true) {
            try {
                Socket s = serverSocket.accept();
                InputStream input = s.getInputStream();
                OutputStream output = s.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                BufferedOutputStream bos = new BufferedOutputStream(output);
                byte[] buffer = new byte[20];
                bis.read(buffer);
                System.out.println(new String(buffer));
                bos.write("Server Echo".getBytes());
                bos.flush();
                s.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // 初始化SSLServerSocket
    // 导入服务端私钥KeyStore，导入服务端受信任的KeyStore(客户端的证书)
    public void init() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            //创建jkd密钥访问库
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            //创建TrustManagerFactory,管理授权证书
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            //创建一个keystore来管理密钥库，keystore的类型，默认是jks
            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");
            //创建jkd密钥访问库	123456是keystore密码。
            ks.load(new FileInputStream("/Users/chaoli/intellijWrkSpace/violin/violin-test/src/main/java/com/wolf/test/ssl/server/kserver.keystore"), SERVER_KEY_STORE_PASSWORD.toCharArray());
            tks.load(new FileInputStream("/Users/chaoli/intellijWrkSpace/violin/violin-test/src/main/java/com/wolf/test/ssl/client/tserver.keystore"), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());
            kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
            tmf.init(tks);
            //KeyManager[] 第一个参数是授权的密钥管理器，用来授权验证。第二个是被授权的证书管理器，用来验证服务器端的证书。
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            serverSocket = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(DEFAULT_PORT);
            serverSocket.setNeedClientAuth(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}