package com.wolf.test.jar;

import org.apache.commons.lang3.StringUtils;

/**
 * <p> Description: 测试打jar包，教程已在word中
 * <p/>
 * Date: 2015/11/9
 * Time: 8:40
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class TestRunJar {
	public static void main(String[] args) {
		System.out.println("xxx");
		String xx = null;
		//为了测试引入第三方jar包
		if (StringUtils.isNotEmpty(xx)) {
			System.out.println("111");
		} else {
			System.out.println("222");
		}
	}
}
