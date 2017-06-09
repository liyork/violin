/**
 * Description: RedisPoolConfig.java
 * All Rights Reserved.

 */
package com.wolf.utils.redis;

/**
 *  pool 参数配置
 * <br/> Created on 2014-7-3 下午3:02:37

 * @since 3.3
 */
public class RedisPoolConfig implements Cloneable{

	private int corePoolSize = 4;
	
    private int maximumPoolSize = 100;
    
    private long keepAliveTime = 60000;
    
    private String host;
    
    private String ip;
    
    private int port = Protocol.DEFAULT_PORT;
    
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    
    private String readHosts ;

	public String getReadHosts() {
		return readHosts;
	}

	public void setReadHosts(String readHosts) {
		this.readHosts = readHosts;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaximumPoolSize() {
		if(this.corePoolSize > this.maximumPoolSize){
			return this.corePoolSize ;
		}
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host.trim();
	}
	
	

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
    
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		return super.clone();
	}
	
}
