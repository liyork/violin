package com.wolf.structural.adapter;

/**
 * <p> Description:模拟已经存在的画文本工具类,没有源码或第三方产品，代码不能修改
 * <p/>
 * Date: 2016/7/4
 * Time: 10:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TextView implements View{

	public void showOnScreen(Double origin, Integer width, Integer height) {
		System.out.println("showOnScreen in TextView using origin and width and height");
	}

	public void isEmpty() {
		System.out.println("isEmpty in TextView");
	}
}