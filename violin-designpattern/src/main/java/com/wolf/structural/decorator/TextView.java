package com.wolf.structural.decorator;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/5
 * Time: 9:12
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TextView implements VisualComponent {
	@Override
	public void draw() {
		System.out.println("draw a text");
	}
}
