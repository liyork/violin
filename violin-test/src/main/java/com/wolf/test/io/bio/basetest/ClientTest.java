package com.wolf.test.io.bio.basetest;

import java.io.*;
import java.net.Socket;

/**
 * Description:
 * <br/> Created on 2017/5/9 13:56
 *
 * @author 李超
 * @since 1.0.0
 */
public class ClientTest {

    public static void main(String args[]) throws Exception {
        String host = "127.0.0.1";  //要连接的服务端IP地址
        int port = 8899;   //要连接的服务端对应的监听端口
        Socket client = new Socket(host, port);
        Writer writer = new OutputStreamWriter(client.getOutputStream());
        writer.write("Hello Server.");
        writer.write("eof");
        writer.flush();//写完后要记得flush

        StringBuilder sb = new StringBuilder();
        char chars[] = new char[64];
        int len;
        System.out.println("11");
        Reader reader = new InputStreamReader(client.getInputStream());
        //会一直卡住
//        while ((len=reader.read(chars)) != -1) {
//            sb.append(new String(chars, 0, len));
//        }
        String temp;
        int index;
        while ((len=reader.read(chars)) != -1) {
            temp = new String(chars, 0, len);
            if ((index = temp.indexOf("eof")) != -1) {
                sb.append(temp.substring(0, index));
                break;
            }
            sb.append(new String(chars, 0, len));
        }
        System.out.println("from server: " + sb);

        System.out.println("22");
        writer.close();
        client.close();
    }
}
