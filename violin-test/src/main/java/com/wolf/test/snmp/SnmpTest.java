package com.wolf.test.snmp;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.SocketException;

/**
 * Description:
 * Created on 2021/3/26 3:45 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SnmpTest {

    public static void main(String[] args) throws Exception {
        //getLocalName();

        firstDemo();
    }

    private static void firstDemo() {
        int version = 2;
        Snmp snmp = null;

        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            if (version == SnmpConstants.version3) {
                // 设置安全模式
                USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
                SecurityModels.getInstance().addSecurityModel(usm);
            }
            // 开始监听消息
            transport.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 构造报文
        PDU pdu = new PDU();
        //PDU pdu = new ScopedPDU();
        // 设置要获取的对象ID，这个OID代表远程计算机的名称
        OID oids = new OID("1.3.6.1.2.1.1.5.0");
        pdu.add(new VariableBinding(oids));
        // 设置报文类型
        pdu.setType(PDU.GET);
        //((ScopedPDU) pdu).setContextName(new OctetString("priv"));
        try {
            // 发送消息 其中最后一个是想要发送的目标地址
            sendMessage(2, snmp, false, true, pdu, "localhost/161");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(int version, Snmp snmp, Boolean syn, final Boolean bro, PDU pdu, String addr)
            throws IOException {
        // 生成目标地址对象
        Address targetAddress = GenericAddress.parse(addr);
        Target target = null;
        if (version == SnmpConstants.version3) {
            // 添加用户
            snmp.getUSM().addUser(new OctetString("MD5DES"), new UsmUser(new OctetString("MD5DES"), AuthMD5.ID, new OctetString("MD5DESUserAuthPassword"), PrivDES.ID, new OctetString("MD5DESUserPrivPassword")));
            target = new UserTarget();
            // 设置安全级别
            ((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
            ((UserTarget) target).setSecurityName(new OctetString("MD5DES"));
            target.setVersion(SnmpConstants.version3);
        } else {
            target = new CommunityTarget();
            if (version == SnmpConstants.version1) {
                target.setVersion(SnmpConstants.version1);
                ((CommunityTarget) target).setCommunity(new OctetString("public"));
            } else {
                target.setVersion(SnmpConstants.version2c);
                ((CommunityTarget) target).setCommunity(new OctetString("public"));
            }

        }
        // 目标对象相关设置
        target.setAddress(targetAddress);
        target.setRetries(5);
        target.setTimeout(1000);

        if (!syn) {
            // 发送报文 并且接受响应
            ResponseEvent response = snmp.send(pdu, target);
            // 处理响应
            System.out.println("Synchronize(同步) message(消息) from(来自) "
                    + response.getPeerAddress() + "\r\n" + "request(发送的请求):"
                    + response.getRequest() + "\r\n" + "response(返回的响应):"
                    + response.getResponse());
        } else {
            // 设置监听对象
            ResponseListener listener = new ResponseListener() {

                public void onResponse(ResponseEvent event) {
                    if (bro.equals(false)) {
                        ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    }
                    // 处理响应
                    PDU request = event.getRequest();
                    PDU response = event.getResponse();
                    System.out.println("Asynchronise(异步) message(消息) from(来自) "
                            + event.getPeerAddress() + "\r\n" + "request(发送的请求):" + request
                            + "\r\n" + "response(返回的响应):" + response);
                }

            };
            // 发送报文
            snmp.send(pdu, target, null, listener);
        }
    }


    private static void getLocalName() throws IOException {
        CommunityTarget myTarget = new CommunityTarget();
        //定义地址
        //Address deviceAdd = GenericAddress.parse("udp:192.168.1.233/161");
        Address localAdd = GenericAddress.parse("udp:localhost/161");
        myTarget.setAddress(localAdd);
        //设置snmp共同体
        myTarget.setCommunity(new OctetString("public"));
        //设置超时重试次数
        myTarget.setRetries(2);
        //设置超时的时间
        myTarget.setTimeout(5 * 60);
        //设置使用的snmp版本
        myTarget.setVersion(SnmpConstants.version2c);
        //设定采取的协议
        TransportMapping transport = new DefaultUdpTransportMapping();//设定传输协议为UDP
        //调用TransportMapping中的listen()方法，启动监听进程，接收消息，由于该监听进程是守护进程，最后应调用close()方法来释放该进程
        transport.listen();
        //创建SNMP对象，用于发送请求PDU
        Snmp protocol = new Snmp(transport);
        //创建请求pdu,获取mib
        PDU request = new PDU();
        //调用的add方法绑定要查询的OID
        request.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1")));
        request.add(new VariableBinding(new OID(new int[]{1, 3, 6, 1, 2, 1, 1, 2})));
        //调用setType()方法来确定该pdu的类型
        request.setType(PDU.GETNEXT);
        //调用 send(PDU pdu,Target target)发送pdu，返回一个ResponseEvent对象
        ResponseEvent responseEvent = protocol.send(request, myTarget);
        //通过ResponseEvent对象来获得SNMP请求的应答pdu，方法：public PDU getResponse()
        PDU response = responseEvent.getResponse();
        if (response != null) {
            System.out.println("request.size()=" + request.size());
            System.out.println("response.size()=" + response.size());
            //通过应答pdu获得mib信息（之前绑定的OID的值），方法：VaribleBinding get(int index)
            VariableBinding vb1 = response.get(0);
            VariableBinding vb2 = response.get(1);
            System.out.println(vb1);
            System.out.println(vb2);
            //调用close()方法释放该进程
            transport.close();
        }
    }
}
