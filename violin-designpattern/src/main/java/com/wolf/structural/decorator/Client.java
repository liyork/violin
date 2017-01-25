package com.wolf.structural.decorator;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/5
 * Time: 9:20
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {
		VisualComponent textView = new TextView();
		VisualComponent decorator = new BorderDecorator(new ScrollBarDecorator(textView));
		decorator.draw();
	}
}
