package com.wolf.companytcp;


import com.wolf.companytcp.client.ClientExecutionHandlerConfig;
import com.wolf.companytcp.server.ServerExecutionHandlerConfig;

public class NettyContainerConfig {
    private static final int DEFAULT_CLIENT_WORKER_COUNT = 4;
    private static final int DEFAULT_SERVER_WORKER_COUNT = 20;
    private static final int DEFAULT_CONNECTIONLESS_WORKER_COUNT = 20;
    private static final int DEFAULT_SERVER_PORT = 9000;
    private static final int DEFAULT_CONNECTIONLESS_PORT = 20000;
    //udp 监听个数，监听端口自动加1
    private static final int DEFAULT_CONNECTIONLESS_SERVER_COUNT = 1;

	private int clientWorkerCount = DEFAULT_CLIENT_WORKER_COUNT;
	
	private int serverWorkerCount = DEFAULT_SERVER_WORKER_COUNT;
	
	//udp work 线程数
	private int connectionlessWorkerCount = DEFAULT_CONNECTIONLESS_WORKER_COUNT;
	
	private int serverPort = DEFAULT_SERVER_PORT;
	//udp 默认端口
	private int connectionlessPort = DEFAULT_CONNECTIONLESS_PORT;
	
	private ServerExecutionHandlerConfig serverExecutionHandlerConfig = null;
	
	private ClientExecutionHandlerConfig clientExecutionHandlerConfig = null;
	//udp 模式server端配置
	private ServerExecutionHandlerConfig connectionlessExecutionHandlerConfig = null;
	
	private int connectionlesServerCount = DEFAULT_CONNECTIONLESS_SERVER_COUNT;
	//配置文件关联的名称，server端："server"，client 端："对应的项目名称",如果没有默认使用server的配置
	private String name = "server";

	private String configName = "nettyConfig";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getConnectionlesServerCount() {
		return connectionlesServerCount;
	}

	public void setConnectionlesServerCount(int connectionlesServerCount) {
		this.connectionlesServerCount = connectionlesServerCount;
	}

	public int getConnectionlessWorkerCount() {
		return connectionlessWorkerCount;
	}

	public void setConnectionlessWorkerCount(int connectionlessWorkerCount) {
		this.connectionlessWorkerCount = connectionlessWorkerCount;
	}

	public int getConnectionlessPort() {
		return connectionlessPort;
	}

	public void setConnectionlessPort(int connectionlessPort) {
		this.connectionlessPort = connectionlessPort;
	}

	public ServerExecutionHandlerConfig getConnectionlessExecutionHandlerConfig() {
		return connectionlessExecutionHandlerConfig;
	}

	public void setConnectionlessExecutionHandlerConfig(
			ServerExecutionHandlerConfig connectionlessExecutionHandlerConfig) {
		this.connectionlessExecutionHandlerConfig = connectionlessExecutionHandlerConfig;
	}

	public int getClientWorkerCount() {
		return clientWorkerCount;
	}

	public void setClientWorkerCount(int clientWorkerCount) {
		this.clientWorkerCount = clientWorkerCount;
	}

	public int getServerWorkerCount() {
		return serverWorkerCount;
	}

	public void setServerWorkerCount(int serverWorkerCount) {
		this.serverWorkerCount = serverWorkerCount;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public ServerExecutionHandlerConfig getServerExecutionHandlerConfig() {
		return serverExecutionHandlerConfig;
	}

	public void setServerExecutionHandlerConfig(ServerExecutionHandlerConfig serverExecutionHandlerConfig) {
		this.serverExecutionHandlerConfig = serverExecutionHandlerConfig;
	}

	public ClientExecutionHandlerConfig getClientExecutionHandlerConfig() {
		return clientExecutionHandlerConfig;
	}

	public void setClientExecutionHandlerConfig(ClientExecutionHandlerConfig clientExecutionHandlerConfig) {
		this.clientExecutionHandlerConfig = clientExecutionHandlerConfig;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}
}
