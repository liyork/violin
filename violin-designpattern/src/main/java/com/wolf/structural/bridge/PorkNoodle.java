package com.wolf.structural.bridge;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/6/19
 */
public class PorkNoodle extends AbstractNoodle {

	public PorkNoodle(PepperStyle pepperStyle) {
		super(pepperStyle);
	}

	@Override
	public void eat() {
		System.out.println("猪肉味"+this.pepperStyle.style());
	}
}
