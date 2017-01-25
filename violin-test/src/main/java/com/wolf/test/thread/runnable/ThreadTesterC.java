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
public class ThreadTesterC implements Runnable {

	private int counter;

	@Override
	public void run() {
		while (counter <= 1000000000) {
			double hypot = Math.hypot(counter, counter);
			Math.atan2(hypot, counter);
			//System.out.print("Counter = " + counter + " ");
			counter++;
		}
		System.out.println();
	}
}
