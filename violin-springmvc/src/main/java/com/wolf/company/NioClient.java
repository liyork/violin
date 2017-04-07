/**
 * Description: NioClient.java
 * All Rights Reserved.
 *

 */
package com.wolf.company;

import org.jboss.netty.channel.Channel;

/**
 * nio client 接口
 */
public interface NioClient {

    void init();


    Channel getChannel(String serviceId);


    Object sendData(Object data, String serviceId);

}
