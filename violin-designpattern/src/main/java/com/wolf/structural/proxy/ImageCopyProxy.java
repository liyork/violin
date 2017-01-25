package com.wolf.structural.proxy;

/**
 * <b>功能</b>copy-on-write
 *
 * @author 李超
 * @Date 2016/7/10
 */
public class ImageCopyProxy implements Image {

	private Image image;
	private String fileName;
	private Extent extent;

	public ImageCopyProxy(Image image) {
		this.image = image;
		this.fileName = image.getFileName();
	}

	@Override
	public void draw() {
		image.draw();
	}

	protected Image getImage() {
		if (image == null) {
			image = new ImageImpl(fileName);
		}
		return image;
	}

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
		this.image = getImage();
		this.image.insertContent(content);
	}
}
