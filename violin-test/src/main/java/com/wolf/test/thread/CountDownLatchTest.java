package com.wolf.test.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/9/17
 * Time: 14:41
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class CountDownLatchTest {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		final CountDownLatch countDownLatch = new CountDownLatch(1);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//countDownLatch.countDown();
			}
		});

		//等5秒,5秒内有调用countDownLatch.countDown()，则true，否则false
		boolean await = countDownLatch.await(5l, TimeUnit.SECONDS);
		if (await) {
			System.out.println("xxx");
		}else{
			System.out.println("yyy");
		}

		executorService.shutdown();
	}
}
