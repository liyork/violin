package com.wolf.behavioral.template;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/18
 * Time: 8:27
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DrawApplication extends AbstractApplication {

	@Override
	protected boolean canOpenDocument(String name) {
		System.out.println("check the name if exist in DrawApplication");
		return true;
	}

	@Override
	protected Document createDocument() {
		return new Document();
	}
}
