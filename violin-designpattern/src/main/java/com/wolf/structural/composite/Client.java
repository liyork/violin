package com.wolf.structural.composite;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/6/19
 */
public class Client {

	public static void main(String[] args) {
		Component leaf1 = new Leaf("leaf1");
		Component leaf2 = new Leaf("leaf2");
		Component leaf3 = new Leaf("leaf3");
		Component composite1 = new Composite("composite1");
		Component composite2 = new Composite("composite2");

		composite1.add(composite2);
		composite1.add(leaf3);
		composite2.add(leaf1);
		composite2.add(leaf2);

		composite1.operation();
	}
}
