package com.wolf.structural.proxy;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/10
 */
public class ImageFactory {

	public static Image createImage(String fileName){
		return new ImageCreateProxy(fileName);
	}

	public static Image copyImage(Image image){
		return new ImageCopyProxy(image);
	}
}
