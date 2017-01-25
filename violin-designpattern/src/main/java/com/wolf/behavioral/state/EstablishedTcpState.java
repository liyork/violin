package com.wolf.behavioral.state;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class EstablishedTcpState extends AbstractTcpState {

	private static EstablishedTcpState establishedTcpState = new EstablishedTcpState();

	@Override
	public void transmit(TcpConnection tcpConnection, String data) {
		System.out.println("transmit in EstablishedTcpState " + tcpConnection.getName() + "," + tcpConnection.getIp() + "," + tcpConnection.getPort());
	}

	@Override
	public void close(TcpConnection tcpConnection) {
		System.out.println("close in EstablishedTcpState " + tcpConnection.getName() + "," + tcpConnection.getIp() + "," + tcpConnection.getPort());
		tcpConnection.changeState(ClosedTcpState.getInstance());
	}

	public static TcpState getInstance() {
		return establishedTcpState;
	}
}
