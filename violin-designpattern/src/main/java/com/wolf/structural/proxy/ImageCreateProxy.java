package com.wolf.structural.proxy;

/**
 * <b>功能</b>构造image非常费时,真正使用时再创建
 *
 * @author 李超
 * @Date 2016/7/9
 */
public class ImageCreateProxy implements Image {

	private ImageImpl image;
	private Extent extent;
	private String fileName;

	public ImageCreateProxy(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void draw() {
		getImage().draw();
	}

	protected ImageImpl getImage() {
		if (image == null) {
			image = new ImageImpl(fileName);
		}
		return image;
	}

	//todo 这里当时没明白作者意图。。uml上是一个意思，但是sample code是另一个意思
	@Override
	public Extent getExtent() {
		if (image == null) {
			extent = getImage().getExtent();
		}
		return extent;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void insertContent(String content) {
		getImage().insertContent(content);
	}

}
