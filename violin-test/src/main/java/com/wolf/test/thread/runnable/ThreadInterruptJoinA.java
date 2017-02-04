package com.wolf.test.thread.runnable;

import com.wolf.test.thread.ThreadTest;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:46
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadInterruptJoinA implements Runnable {

	@Override
	public void run() {
		ThreadTest.simulateLongTimeOperation(7000000);
		System.out.println("ThreadTesterC...");
	}
}
