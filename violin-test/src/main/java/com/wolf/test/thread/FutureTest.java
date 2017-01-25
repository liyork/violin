package com.wolf.test.thread;

import com.wolf.test.thread.runnable.MyTask;

import java.util.concurrent.*;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 9:01
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class FutureTest {

	public static void main(String[] args) {
		System.out.println("Start ...");

		ExecutorService exec = Executors.newCachedThreadPool();

		testTask(exec, 15); // 成功
		testTask(exec, 5); //  失败

		exec.shutdown();
		System.out.println("End!");
	}
	public static void testTask(ExecutorService exec, int timeout) {
		MyTask task = new MyTask();
		Future<Boolean> future = exec.submit(task);
		Boolean taskResult = null;
		String failReason = null;
		try {
			// 等待计算结果，最长等待timeout秒，timeout秒后中止任务
			taskResult = future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			failReason = "主线程在等待计算结果时被中断！";
		} catch (ExecutionException e) {
			failReason = "主线程等待计算结果，但计算抛出异常！";
		} catch (TimeoutException e) {
			failReason = "主线程等待计算结果超时，因此中断任务线程！";
			exec.shutdownNow();
		}

		System.out.println("\ntaskResult : " + taskResult);
		System.out.println("failReason : " + failReason);
	}
}