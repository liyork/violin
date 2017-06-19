package com.wolf.test.concurrent.thread.productandconsumer.manytomany.usersynchronized;

/**
 * <p> Description:这个不太靠谱，还有个未解决的问题。
 * <p/>
 * Date: 2016/6/12
 * Time: 16:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TestProducerAndConsumer {

	public static void main(String[] args){
		Object producerMonitor = new Object();
		Object consumerMonitor = new Object();
		Container<Bread> container = new Container<Bread>(10);
		//生产者开动
		new Thread(new Producer(producerMonitor,consumerMonitor,container)).start();
		new Thread(new Producer(producerMonitor,consumerMonitor,container)).start();
		new Thread(new Producer(producerMonitor,consumerMonitor,container)).start();
		new Thread(new Producer(producerMonitor,consumerMonitor,container)).start();
		//消费者开动
		new Thread(new Consumer(producerMonitor,consumerMonitor,container)).start();
		new Thread(new Consumer(producerMonitor,consumerMonitor,container)).start();
	}
}
