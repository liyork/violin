package com.wolf.structural.adapter;

/**
 * <p> Description:将一个不兼容对象(textView)的方法转换成我们系统使用的接口(shape)的子类型
 * Shape
 * TextShape --->  TextView
 * adapter作用：使用一个其他系统对象扩充当前接口，重用原有其他系统的对象
 *
 * Convert the interface(View) of a class(textView) into another interface(Shape) clients expect.
 * Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.
 *
 * Often the adapter is responsible for functionality the adapted class doesn't provide
 *
 * you want to use an existing class or subclass, and its interface does not match the one you need
 * <p/>
 * Date: 2016/7/4
 * Time: 10:41
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {
		Shape shapeByClass = new TextShapeByClassAdapter();
		shapeByClass.draw(1.1, 1.3);

		Shape shapeByObject = new TextShapeObjectAdapter(new TextView());
		shapeByObject.draw(2.2, 2.3);
	}
}
