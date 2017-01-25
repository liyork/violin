package com.wolf.structural.proxy;

/**
 * <b>功能</b>
 * Provide a surrogate or placeholder for another object to control access to it
 *
 * applicability
 * A remote proxy
 * A virtual proxy
 * A protection proxy
 * A smart reference
 *
 *
 * @author 李超
 * @Date 2016/7/9
 */
public class Client {

	public static void main(String[] args) {
		Image image = ImageFactory.createImage("c.txt");
		//真正用到或修改时才真正创建image
		image.draw();

		Image copyImage = ImageFactory.copyImage(image);
		copyImage.draw();
		//真正用到或者修改属性时才真正创建
		copyImage.insertContent("33");
	}
}
