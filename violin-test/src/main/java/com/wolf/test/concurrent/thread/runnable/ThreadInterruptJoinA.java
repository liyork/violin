package com.wolf.test.concurrent.thread.runnable;

import com.wolf.utils.BaseUtils;

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
		BaseUtils.simulateLongTimeOperation(7000000);
		System.out.println("ThreadTesterC...");
	}
}
