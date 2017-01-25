package com.wolf.structural.bridge;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/6/19
 */
public class BeefNoodle extends AbstractNoodle {

	public BeefNoodle(PepperStyle pepperStyle) {
		super(pepperStyle);
	}

	@Override
	public void eat() {
		System.out.println("牛肉味"+this.pepperStyle.style());
	}
}
