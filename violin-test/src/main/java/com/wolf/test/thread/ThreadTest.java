package com.wolf.test.thread;

import com.wolf.test.thread.runnable.*;

/**
 * <p> Description: 测试终断
 * interrupt()不会中断一个正在运行的线程。interrupt()方法只是改变了线程的运行状态
 * 这一方法实际上完成的是：在线程受到阻塞时抛出一个中断信号，这样线程就得以退出阻塞的状态。更确切的说，
 * 如果线程被Object.wait, Thread.join和Thread.sleep三种方法之一阻塞，
 * 那么，它将接收到一个中断异常（InterruptedException），从而提早地终结被阻塞状态。
 * 一个抛出了InterruptedException的线程的状态马上就会被置为非中断状态
 * 如果线程没有被阻塞，这时调用interrupt()将不起作用
 * <p/>
 * 线程A在执行sleep,wait,join时,线程B调用A的interrupt方法,
 * 的确这一个时候A会有InterruptedException异常抛出来.但这其实是在sleep,wait,join
 * 这些方法内部会不断检查中断状态的值,而自己抛出的InterruptedException。
 * 如果线程A正在执行一些指定的操作时如赋值,for,while,if,调用方法等,都不会去检查中断状态
 * <p/>
 * <p>测试两个线程访问一个对象的两个同步方法，成员方法锁定的是当前对象，静态方法锁定的是类class，不能同时进行,也不区分方法修饰符
 * </p>
 * <p/>
 * Thread.interrupted() 和new Thread().isInterrupted(); 区别：前者会清除标记判断当前是否被打断，后者不会清除标记判断是否被打断
 * <p/>
 * 守护线程也叫精灵线程，当程序只剩下 守护线程的时候 程序就会退出。
 * 守护线程的作用 类似在后台静默执行 ， 比如JVM的垃圾回收机制, 这个就是一个 守护线程。 而非守护线程则不会。
 * main以及其他创建的线程都是非守护线程
 * thread.setDaemon(true)必须在thread.start()之前设置
 * <p/>
 * thread.interrupt();如果放于start之前，则thread.isInterrupted()返回false
 * <p/>
 * JVM会在所有的非守护线程（用户线程）执行完毕后退出；
 * main线程是用户线程；
 * 仅有main线程一个用户线程执行完毕，不能决定JVM是否退出，也即是说main线程并不一定是最后一个退出的线程。
 * <p/>
 * Date: 2015/9/23
 * Time: 17:02
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ThreadTest {

	private static Object monitor = new Object();

	public static void main(String[] args) throws Exception {
//		testDaemon();
//		testInterruptNoBlock();
//		testSynMethod();
//		testJoin1();
//		testJoin3();
//		testWaitShouldInSynScope();

		testInterruptState();

//		testErrorWayToStopThread();
//		testRightWayToStopThread1();
//		testRightWayToStopThread2();
//		testIOReadInterrupt();
	}


	private static void testDaemon() {
		System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().isDaemon());
			}
		});
		thread.start();
	}

	//测试sleep被Interrupt打断
	public static void testInterruptSleep() {
		final Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("111");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("2222");
			}
		});


		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("3333");
				try {
					Thread.sleep(1000);
					thread1.interrupt();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("4444");
			}
		});

		thread1.start();
		thread2.start();
	}


	//测试wait被Interrupt打断
	public static void testInterruptWait() {

		final ThreadTest threadTest = new ThreadTest();

		final Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				threadTest.test1();
			}
		});

		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				threadTest.test2(thread1);
			}
		});

		thread1.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		thread2.start();
	}

	public synchronized void test1() {
		System.out.println("test1...");
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 小心锁定的问题.wait会释放锁,
	// 当对等待中的线程调用interrupt()时(注意是等待的线程调用其自己的interrupt()),
	// 被打断线程会先重新获取锁定,再抛出异常.在获取锁定之前,是无法抛出异常的.
	//testInterruptWait测试中，需要等到test2中4秒过后释放锁，test1中wait才能抛出异常
	public synchronized void test2(final Thread thread1) {
		System.out.println("test2...");
		thread1.interrupt();
		System.out.println(22222);

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void testJoin1() throws InterruptedException {
		System.out.println("before testJoin...");

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					System.out.println(i);
				}
			}
		});

		thread.start();
		//优先执行thread，后才执行main
		thread.join();

		System.out.println("main stop ");

	}

	public static void testJoin2() {
		Thread t1 = new Thread(new ThreadTesterA());
		Thread t2 = new Thread(new ThreadTesterB(t1));
		t1.start();
		t2.start();

		//t1.join在t2中，不会影响main
		System.out.println("main ...");
	}


	public static void testJoin3() throws InterruptedException {
		System.out.println("before testJoin...");

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					System.out.println("xxxx==>" + i);
				}
			}
		});

		final Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					System.out.println("yyyy==>" + i);
				}
			}
		});


		thread.start();
		thread2.start();

		//1和2线程交互执行，但是main最后
		thread.join();
		thread2.join();

		System.out.println("main stop ");

	}

	//一定是阻塞的线程来调用其自己的interrupt方法
	public static void testInterruptJoin() {

		Thread t1 = new Thread(new ThreadTesterC());
		Thread t2 = new Thread(new ThreadTesterB(t1));
		t1.start();
		t2.start();

		System.out.println("1111");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("2222");
		//这里终止t1没用，需要终止t2才可以
		t2.interrupt();
		System.out.println("3333");
	}

	//无阻塞，interrupt()不管用,直到thread遇到阻塞
	public static void testInterruptNoBlock() {

		Thread t1 = new Thread(new ThreadTesterD());
		t1.start();

		System.out.println("2222");
		t1.interrupt();
		System.out.println("3333");
	}


	public static void testInterrupted() {
		//将任务交给一个线程执行
		Thread t = new Thread(new ATask());
		t.start();

		//运行一断时间中断线程
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("****************************");
		System.out.println("Interrupted Thread!");
		System.out.println("****************************");
		t.interrupt();
	}

	//线程一般只有在阻塞前被interrupt，方法isInterrupted才返回true，否则被interrupt之后抛出异常isInterrupted结果又是false
	public static void testInterruptState() {
		Thread t = new Thread(new InterruptedStateRunnable());
		t.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
	}

	private static void testSynMethod() {

		final SynMethodClass twoSynMethodClass = new SynMethodClass();

		new Thread(new Runnable() {
			@Override
			public void run() {
				twoSynMethodClass.test3();
			}
		}).start();

		//模拟长时间
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				twoSynMethodClass.test2();
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				twoSynMethodClass.test4();
			}
		}).start();
	}

	//wait方法需要在同步范围内被调用，否则IllegalMonitorStateException
	private static void testWaitShouldInSynScope() throws InterruptedException {
		monitor.wait();
	}


	private static void testErrorWayToStopThread() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("====>111");
				simulateLongTimeOperation();
				System.out.println("====>222");
			}
		});
		thread.start();


		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("stop the thread..");
		//不建议使用
		thread.stop();
	}

	//可以让非运行时状况的线程停止(sleep、wait、I/O阻塞)
	//注：调用interrupt()的情况是依赖与实际运行的平台的。
	// 在Solaris和Linux平台上将会抛出InterruptedIOException的异常，但是Windows上面不会有这种异常
	private static void testRightWayToStopThread1() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("====>111");
				for (int x = 0; x < 99999999; x++) {
					if (!Thread.currentThread().isInterrupted()) {
						x++;
						int y = x + 1;
						int z = y * 333;
						//这里如果还有wait,sleep,join还需要try
						int c = z + 333 - 999 * 333 / 4444;
						int i = c * 45559 * 2232 - 22;
						System.out.println(i);
					}
				}
				System.out.println("====>222");
			}
		});
		thread.start();


		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("stop the thread..");
		thread.interrupt();
	}


	private static void testRightWayToStopThread2() {
		TestStopRunnable testStopRunnable = new TestStopRunnable();
		Thread thread = new Thread(testStopRunnable);
		thread.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("stop the thread..");
		testStopRunnable.setNeedRun(false);
	}


	//Io阻塞：线程调用了IO的read操作或者socket的accept操作，处于阻塞状态。
	//不能处理打断写大文件操作，所以分段写入
	private static void testIOReadInterrupt() {
		try {
			System.out.println("Enter lines of input (user ctrl+Z Enter to terminate):");
			System.out.println("(Input thread will be interrupted in 10 sec.)");
			// interrupt input in 10 sec
			Thread thread = new Thread(new ReadLineRunnable());
			thread.start();

			Thread.sleep(3000);
			thread.interrupt();
		} catch (Exception ex) {
			System.out.println(ex.toString()); // printStackTrace();
		}
	}

	private static void simulateLongTimeOperation(){
		for (int x = 0; x < 99999999; x++) {
			x ++;
			int y = x+1;
			int z = y *335553;
			int c = z+5555333-955599*3344443;
			int i = c * 45559 * 2232 - 22;
			z = (i *232168-12379791)/12373;
			int qq= z;
		}
	}

}
