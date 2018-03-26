package com.wolf.test.concurrent.thread.runnable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:50
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Cat implements Runnable{

	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName()+" begin run ... time:"+System.currentTimeMillis());
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName()+" catch run ..."+System.currentTimeMillis());
		}
	}
}