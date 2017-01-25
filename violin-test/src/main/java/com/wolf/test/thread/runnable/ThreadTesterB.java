package com.wolf.test.thread.runnable;

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
public class ThreadTesterB implements Runnable {

	public ThreadTesterB(Thread t1) {
		this.t1 = t1;
	}

	private int i;

	private Thread t1;

	@Override
	public void run() {
		System.out.println("tb ...");
		try {
			//t1加入，则先执行t1,t1执行完后再执行后面
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (i <= 10) {
			System.out.print("i = " + i + " ");
			i++;
		}
		System.out.println();
	}
}
