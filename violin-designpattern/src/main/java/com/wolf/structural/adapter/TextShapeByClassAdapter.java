package com.wolf.structural.adapter;

/**
 * <p> Description:使TextView兼容目前有的系统(使用Shape对象)
 * 通过类继承方式实现adapter,继承adaptee用来使用它的方法。
 * 如果需要适配其他子类，则需要继承其他子类，类扩大了
 *
 * lets Adapter override some of Adaptee's behavior, since Adapter is a subclass of Adaptee
 * introduces only one object, and no additional pointer indirection is needed to get to the adaptee
 * <p/>
 * Date: 2016/7/4
 * Time: 10:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TextShapeByClassAdapter extends TextView implements Shape {

	@Override
	public void draw(Double bottomLeft, Double topRight) {
		System.out.println("draw in TextShapeAdapter using bottomLeft:"+bottomLeft+" and topRight:"+topRight);
		Double origin = computeOrigin(bottomLeft, topRight);
		super.showOnScreen(origin,computeWidth(origin,bottomLeft, topRight),computeHeight(bottomLeft, topRight));
	}

	private Integer computeHeight(Double bottomLeft, Double topRight) {
		return 2;
	}

	private Integer computeWidth(Double origin, Double bottomLeft, Double topRight) {
		return 3;
	}

	private Double computeOrigin(Double bottomLeft, Double topRight) {
		return 1.1;
	}

	@Override
	public void getManipulator() {
		System.out.println("getManipulator in TextShapeClassAdapter");
	}
}
