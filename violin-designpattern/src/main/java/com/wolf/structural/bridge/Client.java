package com.wolf.structural.bridge;

/**
 * <b>功能</b>
 * 某个类具有两个或两个以上的维度变化，如果只是用继承将无法实现这种需要，或者使得设计变得相当臃肿
 * 桥接模式的做法是把一个维度的变化抽象出来，最后，提供一个管理类来组合不同维度上的变化，通过这种组合来满足业务的需要。
 * 使用组合方式添加替换另一个维度的可变因子
 * <p/>
 * The Adapter (139) pattern is geared toward making unrelated classes work
 * together. It is usually applied to systems after they're designed. Bridge, on
 * the other hand, is used up-front in a design to let abstractions and
 * implementations vary independently
 *
 * @author 李超
 * @Date 2016/6/19
 */
public class Client {

	public static void main(String[] args) {
		PepperStyle pepperStyle = new HardStyle();
		BeefNoodle beefNoodle = new BeefNoodle(pepperStyle);
		beefNoodle.eat();
	}
}
