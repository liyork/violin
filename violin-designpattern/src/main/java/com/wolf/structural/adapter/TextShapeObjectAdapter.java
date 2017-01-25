package com.wolf.structural.adapter;

/**
 * <p> Description:使TextView兼容目前有的系统(使用Shape对象)
 * 通过组合对象方式实现adapter，通过(外界传入)成员变量adaptee代理调用。
 * 如果需要适配其他子类，则放入其他子类成员变量即可。也可以放入父类变量，具体方法再判断分别执行
 *
 * lets a single Adapter work with many Adaptees—that is, the Adaptee itself and all of its subclasses (if any)
 * makes it harder to override Adaptee behavior It will require subclassing Adaptee and making Adapter refer to
 * 		the subclass rather than the Adaptee itself.
 * <p/>
 * Date: 2016/7/4
 * Time: 10:16
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TextShapeObjectAdapter implements Shape {

	private TextView textView;

	public TextShapeObjectAdapter(TextView textView) {
		this.textView = textView;
	}

	@Override
	public void draw(Double bottomLeft, Double topRight) {
		System.out.println("draw in TextShapeObjectAdapter using bottomLeft:"+bottomLeft+" and topRight:"+topRight);
		Double origin = computeOrigin(bottomLeft, topRight);
		textView.showOnScreen(origin, computeWidth(origin, bottomLeft, topRight), computeHeight(bottomLeft, topRight));
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
