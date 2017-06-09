/**
 * Description: ClientConfigUtils.java
 * All Rights Reserved.
 *

 */
package com.wolf.companytcp.client;


import com.wolf.utils.LoadHierarchyProperties;
import com.wolf.companytcp.NettyContainerConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析客户端配置，提供获取指定项目的配置
 */
public final class ClientConfigUtils {

    private static final Map<String, NettyContainerConfig> CLIENT_MAP = new HashMap<String, NettyContainerConfig>();

    private static NettyContainerConfig serverConfig = null;

    private ClientConfigUtils() {
    }

    static {
        List<Object> list = LoadHierarchyProperties.loadHierarchy("nettyConfig");
        if(list == null) {
            throw new RuntimeException("nettyconfig config is null!");
        }
        for(Object aList : list) {
            NettyContainerConfig config = (NettyContainerConfig) aList;
            if("server".equals(config.getName())) {
                serverConfig = config;
            }
            if(!StringUtils.isEmpty(config.getName())) {
                CLIENT_MAP.put(config.getName(), config);
            }
        }
    }

    /**
     * 客户端获取指定的config
     */
    public static NettyContainerConfig getClientConfig(String name) {

        NettyContainerConfig config = CLIENT_MAP.get(name);
        if(config == null) {
            return serverConfig;
        }
        return config;
    }

}
