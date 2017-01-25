package com.wolf.test.thread;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/23
 * Time: 11:48
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class SynMethodClass {

	public void test3(){
		System.out.println("test3...");
		test1();
	}

	private synchronized void test1(){
		System.out.println("test1...");
		//模拟长时间
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	synchronized void test2(){
		System.out.println("test2...");
	}

	synchronized static void test4(){
		System.out.println("test4...");
	}
}
