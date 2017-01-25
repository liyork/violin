package com.wolf.structural.decorator;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/5
 * Time: 9:15
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class BorderDecorator extends VisualAbstractDecorator implements VisualComponent {

	public BorderDecorator(VisualComponent visualComponent) {
		super(visualComponent);
	}

	@Override
	public void draw() {
		addBorder();
		visualComponent.draw();
	}

	private void addBorder() {
		System.out.println("addBorder..");
	}

}
