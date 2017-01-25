package com.wolf.test.thread.runnable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:47
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadTesterD implements Runnable {

	private int counter;

	@Override
	public void run() {
		//模拟很长时间
		while (counter <= 5000000) {
			double hypot = Math.hypot(counter, counter);
			Math.atan2(hypot, counter);
			//System.out.print("Counter = " + counter + " ");
			counter++;
		}
		System.out.println("counter==>"+counter);
		try {
			//抛出异常，由于主线程中调用了t1.interrupt
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}