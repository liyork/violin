package com.wolf.test.writeexam.b;

/**
 * Description:
 * <br/> Created on 2017/4/23 11:11
 *
 * @author 李超
 * @since 1.0.0
 */
public class Test3 {

	public static void main(String[] args) {
		//由于a是默认即包访问权限，只有同一个包下的类才能访问
//		int a = new Test1().a;
		//由于b是protected访问权限，只有同包或者其他地方的子类才能访问
//		new Test1().b
	}
}
