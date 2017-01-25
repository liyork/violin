package com.wolf.structural.proxy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/9
 */
public class ImageImpl implements Image {

	private Extent extent;

	public ImageImpl(String fileName) {
		//构造image非常费时
		System.out.println("this operation is too expensive");
		//值从file中取得
		this.extent = new Extent(1, 1);
	}

	@Override
	public void draw() {
		System.out.println("draw in ImageImpl...");
	}

	@Override
	public Extent getExtent() {
		return extent;
	}

	@Override
	public String getFileName() {
		return null;
	}

	@Override
	public void insertContent(String content) {
		System.out.println("insert cotent to file in ImageImpl...");
	}
}
