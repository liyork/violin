package com.wolf.behavioral.state;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class TcpConnection {

	private String name;

	private String ip;

	private String port;

	private TcpState tcpState;

	public TcpConnection(String name, String ip, String port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		tcpState = ClosedTcpState.getInstance();
	}

	public void activeOpen(){
		tcpState.activeOpen(this);
	}
	public void passiveOpen(){
		tcpState.passiveOpen(this);
	}
	public void transmit(String data){
		tcpState.transmit(this,data);
	}
	public void send(){
		tcpState.send(this);
	}
	protected void changeState(TcpState tcpState) {
		this.tcpState = tcpState;
	}
	public void close(){
		tcpState.close(this);
	}

	public String getName() {
		return name;
	}

	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}
}
