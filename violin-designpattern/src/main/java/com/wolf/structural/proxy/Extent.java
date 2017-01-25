package com.wolf.structural.proxy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/9
 */
public class Extent {

	private int width;

	private int height;

	public Extent(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
