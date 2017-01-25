package com.wolf.behavioral.state;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class ListenTcpState extends AbstractTcpState {

	private static ListenTcpState listenTcpState = new ListenTcpState();

	@Override
	public void send(TcpConnection tcpConnection) {
		System.out.println("send in ListenTcpState " + tcpConnection.getName() + "," + tcpConnection.getIp() + "," + tcpConnection.getPort());
		tcpConnection.changeState(EstablishedTcpState.getInstance());
	}

	public static TcpState getInstance() {
		return listenTcpState;
	}
}
