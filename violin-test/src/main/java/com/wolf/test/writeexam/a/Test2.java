package com.wolf.test.writeexam.a;

/**
 * Description:
 * <br/> Created on 2017/4/23 11:11
 *
 * @author 李超
 * @since 1.0.0
 */
public class Test2 {

	public static void main(String[] args) {
		//同包下的类Test2，包访问权限和protected权限都能访问
		Test1 test1 = new Test1();
		int a = test1.a;

		int b = test1.b;
	}
}
