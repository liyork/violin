package com.wolf.structural.bridge;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/6/19
 */
public abstract class AbstractNoodle implements Noodle{

	protected PepperStyle pepperStyle;

	public AbstractNoodle(PepperStyle pepperStyle) {
		this.pepperStyle = pepperStyle;
	}
}
