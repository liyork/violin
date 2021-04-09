package com.wolf.test.snmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * Description:
 * Created on 2021/4/3 2:26 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SnmpTest {
    private static Logger log = LoggerFactory.getLogger(SnmpTest.class);
    public static Snmp snmp = null;
    private static String community = "public";
    private static String ipAddress = "udp:172.17.166.27/";

    public static void initSnmp() throws IOException {
        MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
        //增加三种处理模型
        messageDispatcher.addMessageProcessingModel(new MPv1());
        messageDispatcher.addMessageProcessingModel(new MPv2c());
        //当要支持snmpV3版本时，需要配置user
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(), localEngineID, 0);
        UsmUser user = new UsmUser(new OctetString("SNMPV3"), AuthSHA.ID, new OctetString("authPassword"),
                PrivAES128.ID, new OctetString("privPassword"));
        usm.addUser(user.getSecurityName(), user);
        messageDispatcher.addMessageProcessingModel(new MPv3(usm));
        //2、创建transportMapping
        // 监听本地
        //UdpAddress updAddr = (UdpAddress) GenericAddress.parse("udp:localhost/1611");
        //TransportMapping<?> transportMapping = new DefaultUdpTransportMapping(updAddr);//传输协议为UDP
        // 不监听本地
        TransportMapping<?> transportMapping = new DefaultUdpTransportMapping();
        //3、创建snmp
        snmp = new Snmp(messageDispatcher, transportMapping);
        //开启监听
        snmp.listen();// 注意close
    }

    private static Target createTarget(int version, int port) {
        Target target = null;
        if (version != SnmpConstants.version3 && version != SnmpConstants.version2c && version != SnmpConstants.version1) {
            log.error("参数version异常");
            return null;
        }
        if (version == SnmpConstants.version3) {
            target = new UserTarget();
            //snmpV3需要设置安全级别和安全名称，其中安全名称是创建snmp指定user设置的new OctetString("SNMPV3")
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            target.setSecurityName(new OctetString("SNMPV3"));
        } else {
            //snmpV1和snmpV2需要指定community
            target = new CommunityTarget();
            ((CommunityTarget) target).setCommunity(new OctetString(community));
            if (version == SnmpConstants.version2c) {
                target.setSecurityModel(SecurityModel.SECURITY_MODEL_SNMPv2c);
            }
        }
        target.setVersion(version);
        target.setAddress(GenericAddress.parse(ipAddress + port));
        target.setRetries(5);
        target.setTimeout(3000);
        return target;
    }

    private static PDU createPDU(int version, int type, String oid) {
        PDU pdu = null;
        if (version == SnmpConstants.version3) {
            pdu = new ScopedPDU();
        } else {
            pdu = new PDUv1();
        }
        pdu.setType(type);
        //可以添加多个变量oid
        pdu.add(new VariableBinding(new OID(oid)));
        return pdu;
    }

    public static void snmpGet(String oid, boolean isSync) throws IOException {
        try {
            //1、初始化snmp,并开启监听
            initSnmp();
            //2、创建目标对象
            Target target = createTarget(SnmpConstants.version2c, SnmpConstants.DEFAULT_COMMAND_RESPONDER_PORT);
            //3、创建报文
            PDU pdu = createPDU(SnmpConstants.version2c, PDU.GET, oid);
            System.out.println("-------> 发送PDU <-------");
            if (isSync) {
                //4、发送报文，并获取返回结果
                ResponseEvent response = snmp.send(pdu, target);
                // 处理响应
                System.out.println("Synchronize(同步) message(消息) from(来自) "
                        + response.getPeerAddress() + "\r\n" + "request(发送的请求):"
                        + response.getRequest() + "\r\n" + "response(返回的响应):"
                        + response.getResponse());
            } else {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                // 设置监听对象
                ResponseListener listener = event -> {
                    //if (bro.equals(false)) {
                    //    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    //}
                    // 处理响应
                    PDU request = event.getRequest();
                    PDU response = event.getResponse();
                    System.out.println("Asynchronise(异步) message(消息) from(来自) "
                            + event.getPeerAddress() + "\r\n" + "request(发送的请求):" + request
                            + "\r\n" + "response(返回的响应):" + response);
                    countDownLatch.countDown();
                };
                // 发送报文
                snmp.send(pdu, target, null, listener);

                countDownLatch.await();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            snmp.close();
        }
    }

    public static void snmpWalk(String oid) {
        try {
            //1、初始化snmp,并开启监听
            initSnmp();
            //2、创建目标对象
            Target target = createTarget(SnmpConstants.version2c, SnmpConstants.DEFAULT_COMMAND_RESPONDER_PORT);
            //3、创建报文
            PDU pdu = createPDU(SnmpConstants.version2c, PDU.GETNEXT, oid);
            System.out.println("-------> 发送PDU <-------");
            //4、发送报文，并获取返回结果
            boolean matched = true;
            while (matched) {
                ResponseEvent responseEvent = snmp.send(pdu, target);
                if (responseEvent == null || responseEvent.getResponse() == null) {
                    break;
                }
                PDU response = responseEvent.getResponse();
                String nextOid = null;
                Vector<? extends VariableBinding> variableBindings = response.getVariableBindings();
                for (int i = 0; i < variableBindings.size(); i++) {
                    VariableBinding variableBinding = variableBindings.elementAt(i);
                    Variable variable = variableBinding.getVariable();
                    nextOid = variableBinding.getOid().toDottedString();
                    // 若不是oid节点下的则退出
                    if (!nextOid.startsWith(oid)) {
                        matched = false;
                        break;
                    }
                }
                if (!matched) {
                    break;
                }
                pdu.clear();
                pdu.add(new VariableBinding(new OID(nextOid)));
                System.out.println("返回结果：" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        // sysDescr
        //snmpGet("1.3.6.1.2.1.1.1.0", true);
        //snmpGet("1.3.6.1.2.1.1.1.0", false);

        // system组，snmpwalk -v 2c -c public localhost system
        //snmpWalk("1.3.6.1.2.1.1");
    }
}