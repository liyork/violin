package com.wolf.test.writeexam.b;

import com.wolf.test.writeexam.a.Test1;

/**
 * Description:
 * <br/> Created on 2017/4/23 11:11
 *
 * @author 李超
 * @since 1.0.0
 */
public class Test4 extends Test1 {

	public static void main(String[] args) {
		//由于a是默认即包访问权限，只有同一个包下的类才能访问,不论是不是有继承关系
//		int a = new Test1().a;

		new Test4().test();
	}

	//protected权限，只要是子类都能访问，不论包所在位置
	private void test() {
		int b = super.b;
	}
}
