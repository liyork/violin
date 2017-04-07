package com.wolf.company.server;


import com.wolf.company.LoadHierarchyProperties;
import com.wolf.company.NettyContainerConfig;
import com.wolf.company.StartInit;

import javax.servlet.ServletContextEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 启动netty<br/> Created on 2013-9-9 上午10:34:30
 *
 */
public class StartNettyServer extends StartInit {
    public static NettyContainerConfig nettyConfig = null;

    public static Map<String, NettyContainerConfig> nettyConfigMap = new HashMap<String, NettyContainerConfig>();

    @Override
    public void execute(ServletContextEvent context) {
        //加载netty带有层级的配置文件
        nettyConfig = loadServerConfig();
        if(nettyConfig == null) {
            return;
        }

        nettyConfigMap.put(nettyConfig.getConfigName(), nettyConfig);

        //tcp server 启动
        if(nettyConfig.getServerExecutionHandlerConfig() != null && nettyConfig.getServerExecutionHandlerConfig().isStart()) {
            nettyConfig.getServerExecutionHandlerConfig().initExecutionHandler();
            TcpServer server = new TcpServer(nettyConfig.getConfigName());
            server.start();
        }
        //udp server 启动
        if(nettyConfig.getConnectionlessExecutionHandlerConfig() != null && nettyConfig.getConnectionlessExecutionHandlerConfig().isStart()) {
            nettyConfig.getConnectionlessExecutionHandlerConfig().initExecutionHandler();
            for(int i = 0; i < nettyConfig.getConnectionlesServerCount(); i++) {
                UdpServer server = UdpServer.getInstance();
                int port = nettyConfig.getConnectionlessPort();
                server.start(port + i);
            }

        }

    }

    public static NettyContainerConfig getNettyConfig() {
        if(nettyConfig == null) {
            throw new RuntimeException("nettyConfig文件配置错误,请检查!");
        } else {
            return nettyConfig;
        }
    }

    public static NettyContainerConfig getNettyConfig(String configName) {
        NettyContainerConfig nettyConfig = nettyConfigMap.get(configName);

        if(nettyConfig == null) {
            throw new RuntimeException("nettyConfig文件配置错误,请检查!");
        } else {
            return nettyConfig;
        }
    }

    private NettyContainerConfig loadServerConfig() {

        String configName = "nettyConfig";

        if(getParam() != null && getParam().length() > 0) {
            configName = getParam();
        }

        List<Object> list = LoadHierarchyProperties.loadHierarchy(configName);
        if(list == null) {
            throw new RuntimeException(configName + " config is null!");
        }
        for(int i = 0; i < list.size(); i++) {
            NettyContainerConfig config = (NettyContainerConfig) list.get(i);
            if("server".equals(config.getName())) {
                config.setConfigName(configName);
                return config;
            }
        }

        throw new RuntimeException("no 'server' config ,please examine!");
    }


}
