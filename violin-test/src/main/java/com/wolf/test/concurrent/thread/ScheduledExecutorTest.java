package com.wolf.test.concurrent.thread;

import com.wolf.test.concurrent.thread.runnable.Cat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p> Description: ScheduledExecutorService 优于timer，线程池比多个timer省资源
 * <p/>
 * Date: 2015/11/13
 * Time: 13:57
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ScheduledExecutorTest {

	public static void main(String[] args) {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
		for (int i = 0; i < 1; i++) {
			//如果上一个任务晚了，就在上一个任务结束后再延迟2秒执行
			executorService.scheduleWithFixedDelay(new Cat(), 10, 20, TimeUnit.SECONDS);
			//如果上一个任务晚了，就在上一个任务结束马上执行
			//executorService.scheduleAtFixedRate(new Cat(), 1, 2, TimeUnit.SECONDS);
		}

	}
}
