package com.wolf.behavioral.strategy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class CarWay implements TripMode {
	@Override
	public void toWork(People people) {
		System.out.println(people.getPrefix()+" by car ");
	}
}
