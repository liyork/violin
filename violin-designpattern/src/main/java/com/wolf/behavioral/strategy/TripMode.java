package com.wolf.behavioral.strategy;

/**
 * <b>功能</b>
 * Define a family of algorithms, encapsulate each one, and make them interchangeable.
 * Strategy lets the algorithm vary independently from clients that use it.
 *
 * 封装变化
 *
 * Sometimes you can reduce this(Increased number of objects.) overhead by
 * implementing strategies as stateless objects that contexts can share.
 *
 * @author 李超
 * @Date 2016/7/17
 */
public interface TripMode {

	//传递people，耦合两者关系，使得本策略不能被重用其他地方，传递参数则可重用，但是不利于扩展
	//The needs of the particular algorithm and its data requirements will determine the best technique.
	public void toWork(People people);
}
