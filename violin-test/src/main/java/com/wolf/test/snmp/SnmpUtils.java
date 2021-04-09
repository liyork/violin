package com
        .wolf.test.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;

/**
 * Description:
 * Created on 2021/4/6 10:50 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SnmpUtils {

    public static CommunityTarget createTarget(String ip, String community, int version) {
        String address = "udp:" + ip + "/20000";
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);

        // retry times when commuication error
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(version);
        return target;
    }
}
