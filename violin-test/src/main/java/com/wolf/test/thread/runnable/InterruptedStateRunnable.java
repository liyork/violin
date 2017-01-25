package com.wolf.test.thread.runnable;

/**
 * <p> Description:用于测试线程阻塞被interrupt后状态恢复正常
 * <p/>
 * Date: 2016/6/23
 * Time: 11:47
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class InterruptedStateRunnable implements Runnable {

	public void run() {

		//这里也可以使用一个成员变量记录是否死循环下去。
		while (!Thread.currentThread().isInterrupted()) {
			System.out.println(Thread.currentThread().getName()+"1111:"+ Thread.currentThread().isInterrupted());
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName()+"2222:"+ Thread.currentThread().isInterrupted());
				//The interrupted status of the current thread is cleared when this exception is thrown
				e.printStackTrace();
				//由于抛出异常会重置打断状态，所以需要再设定，不然就会退不出循环了。。。
				Thread.currentThread().interrupt();

			}
			System.out.println(Thread.currentThread().getName()+"3333:"+ Thread.currentThread().isInterrupted());
		}
		System.out.println("ATask.run() interrupted!");
	}
}
