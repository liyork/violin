package com.wolf.test.concurrent.thread.productandconsumer.manytomany.test1;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/12
 * Time: 15:12
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Consumer extends Thread {
	// 每次消费的产品数量
	private int num;

	// 所在放置的仓库
	private Storage storage;

	// 构造函数，设置仓库
	public Consumer(Storage storage) {
		this.storage = storage;
	}

	// 线程run函数
	public void run() {
		while (true) {
			consume(num);
		}

	}

	// 调用仓库Storage的生产函数
	public void consume(int num) {
		storage.consume(num);
	}

	// get/set方法
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}
}