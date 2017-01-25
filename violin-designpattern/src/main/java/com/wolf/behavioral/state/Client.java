package com.wolf.behavioral.state;

/**
 * <b>功能</b>
 * Allow an object to alter its behavior when its internal state changes. The
 * object will appear to change its class.
 *
 * applicability
 * An object's behavior depends on its state, and it must change its behavior at
 * run-time depending on that state.
 * or
 * Operations have large, multipart conditional statements that depend on the object's state.
 * it also can eliminate the duplication code.
 *
 * It localizes state-specific behavior and partitions behavior for different states.
 * 对于一个对象要被调用多次，每次依赖不同状态，此模式非常有用。可代替长串的if else
 * The State pattern offers a better way to structure state-specific code and makes its intent clearer
 * It makes state transitions explicit.
 * The State pattern is more specific, focusing on how to deal with an object whose behavior
 * depends on its state.
 *
 *
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class Client {

	public static void main(String[] args) {
		TcpConnection tcpConnection = new TcpConnection("connection","127.0.0.1","8080");
		tcpConnection.activeOpen();
		tcpConnection.transmit("transmit data...");
		tcpConnection.close();
	}
}
