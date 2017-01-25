package com.wolf.test.thread.runnable;

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
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("shit");
		}
		System.out.println(Thread.currentThread().getName()+"我起来了");
		System.out.println(Thread.currentThread().getName()+"我回去了");
	}
}