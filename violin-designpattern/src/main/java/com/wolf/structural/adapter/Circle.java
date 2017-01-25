package com.wolf.structural.adapter;

/**
 * <p> Description:由于adapter是适配adaptee到我们使用的目标类，而非对象,混乱了视野
 * <p/>
 * Date: 2016/7/4
 * Time: 10:10
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Circle implements Shape {
	@Override
	public void draw(Double bottomLeft, Double topRight) {
		System.out.println("draw in Circle using bottomLeft and topRight");
	}

	@Override
	public void getManipulator() {

	}
}
