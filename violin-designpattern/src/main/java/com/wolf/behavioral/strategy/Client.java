package com.wolf.behavioral.strategy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/17
 */
public class Client {

	public static void main(String[] args) {
		People people = new People();
		people.setTripMode(new CarWay());
		people.goToWork();

		people.setTripMode(new FootWay());
		people.goToWork();
	}
}
