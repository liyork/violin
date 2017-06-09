package com.wolf.test.concurrent.thread;

import com.wolf.test.concurrent.thread.runnable.SearchTask;

import java.util.concurrent.TimeUnit;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/9/14
 * Time: 9:43
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadGroupTest {

	public static void main(String[] args) {
		//创建5个线程，并入group里面进行管理
		ThreadGroup threadGroup = new ThreadGroup("Searcher");
		SearchTask searchTask = new SearchTask();
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(threadGroup, searchTask);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//通过这种方法可以看group里面的所有信息
		System.out.printf("Number of Threads: %d\n", threadGroup.activeCount());
		System.out.printf("Information about the Thread Group==============\n");
		System.out.println("list start............");
		threadGroup.list();
		System.out.println("list end............");

		//这样可以复制group里面的thread信息
		Thread[] threads = new Thread[threadGroup.activeCount()];
		threadGroup.enumerate(threads);
		for (int i = 0; i < threadGroup.activeCount(); i++) {
			System.out.printf("Thread %s: %s\n", threads[i].getName(),
					threads[i].getState());
		}

		waitFinish(threadGroup);
		//将group里面的所有线程都给interpet
		threadGroup.interrupt();

		ThreadGroup threadGroup1 = Thread.currentThread().getThreadGroup();
		System.out.println("main group ====>"+threadGroup1.getName());

	}

	private static void waitFinish(ThreadGroup threadGroup) {
		while (threadGroup.activeCount() > 9) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
