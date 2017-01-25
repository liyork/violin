package com.wolf.test.thread.productandconsumer.onetoone.useblockingqueue;

import java.util.concurrent.BlockingQueue;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/12
 * Time: 12:59
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Producer implements Runnable{

	private BlockingQueue<Food> foods;
	Producer(BlockingQueue<Food> foods) {
		this.foods = foods;
	}

	@Override
	public void run() {
		int i = 0;
		while (true){
			try {
				//当生产的食品数量装满了容器，那么在while里面该食品容器(阻塞队列)会自动阻塞  wait状态 等待消费
				foods.put(new Food("食品"+i));
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
	}
}
