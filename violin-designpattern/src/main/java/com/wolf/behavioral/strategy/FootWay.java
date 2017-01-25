package com.wolf.behavioral.strategy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class FootWay implements TripMode {
	@Override
	public void toWork(People people) {
		System.out.println(people.getPrefix()+" by foot ");
	}
}
