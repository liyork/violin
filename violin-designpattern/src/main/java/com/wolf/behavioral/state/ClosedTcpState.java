package com.wolf.behavioral.state;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class ClosedTcpState extends AbstractTcpState {

	private static ClosedTcpState closedTcpState = new ClosedTcpState();

	@Override
	public void passiveOpen(TcpConnection tcpConnection) {
		System.out.println("passiveOpen in ClosedTcpState "+tcpConnection.getName()+","+tcpConnection.getIp()+","+tcpConnection.getPort());
		tcpConnection.changeState(ListenTcpState.getInstance());
	}

	@Override
	public void activeOpen(TcpConnection tcpConnection) {
		System.out.println("activeOpen in ClosedTcpState " + tcpConnection.getName() + "," + tcpConnection.getIp() + "," + tcpConnection.getPort());
		tcpConnection.changeState(EstablishedTcpState.getInstance());
	}

	public static TcpState getInstance() {
		return closedTcpState;
	}
}
