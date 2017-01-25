package com.wolf.creational.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/3
 */
public class Client {

	public static void main(String[] args) {
//		WindowManager instance = WindowManager.getInstance();
//		System.out.println(instance);

		WindowManagerOther instance = WindowManagerOther.getInstance();
		System.out.println(instance);

//		testIfSingleton();
	}

	private static void testIfSingleton() {
		final Map<String, WindowManager> map = new ConcurrentHashMap<String, WindowManager>();
		final CountDownLatch countDownLatch = new CountDownLatch(20);

		ExecutorService executorService = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					countDownLatch.countDown();

					WindowManager instance = WindowManager.getInstance();
					map.put(instance.toString(), instance);
				}
			});
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(map.size());

		executorService.shutdownNow();
	}
}
