package com.wolf.test.concurrent.thread.runnable;

import java.util.concurrent.Callable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:51
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class MyTask implements Callable<Boolean> {

	@Override
	public Boolean call() throws Exception {
		// 总计耗时约10秒
		for (int i = 0; i < 100L; i++) {
			Thread.sleep(100); // 睡眠0.1秒
			System.out.print('-');
		}
		return Boolean.TRUE;
	}
}