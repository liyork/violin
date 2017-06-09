package com.wolf.test.concurrent.thread.productandconsumer.onetoone.useblockingqueue;

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
public class Food {

	private String name;

	String getName() {
		return name;
	}

	Food(String name){
		this.name = name;
		System.out.println("生产"+name);
	}
}
