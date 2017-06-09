package com.wolf.test.concurrent.thread.runnable;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
public class SearchTask implements Runnable {

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		System.out.println("Thread Start " + name);
		try {
			doTask();
		} catch (InterruptedException e) {
			System.out.printf("Thread %s: Interrupted\n", name);
			return;
		}
		System.out.println("Thread end " + name);
	}

	private void doTask() throws InterruptedException {
		Random random = new Random((new Date()).getTime());
		int value = (int) (random.nextDouble() * 100);
		System.out.printf("Thread %s: %d\n", Thread.currentThread().getName(),
				value);
		TimeUnit.SECONDS.sleep(value);
	}
}