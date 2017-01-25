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
public class ScrollBarDecorator extends VisualAbstractDecorator implements VisualComponent {

	public ScrollBarDecorator(VisualComponent visualComponent) {
		super(visualComponent);
	}

	@Override
	public void draw() {
		addScrollBar();
		visualComponent.draw();
	}

	private void addScrollBar() {
		System.out.println("addScrollBar...");
	}

}
