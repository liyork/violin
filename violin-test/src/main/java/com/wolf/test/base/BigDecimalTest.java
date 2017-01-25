package com.wolf.test.base;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p> Description:
 * <p/>
 * Date: 2015/9/2
 * Time: 10:02
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class BigDecimalTest {

	public static void main(String[] args) {
		//参数不推荐使用非字符串
		BigDecimal bigDecimalx = new BigDecimal(2.336);
		System.out.println(bigDecimalx);

		//参数推荐使用字符串
		BigDecimal bigDecimal = new BigDecimal("2.335");
		//四舍五入,如果是5向下舍
		BigDecimal bigDecimal1 = bigDecimal.setScale(2, RoundingMode.HALF_DOWN);
		System.out.println("HALF_DOWN==>"+bigDecimal1);
		//四舍五入
		BigDecimal bigDecimal3 = bigDecimal.setScale(2, RoundingMode.HALF_UP);
		System.out.println("HALF_UP==>"+bigDecimal3);
		//直接舍去
		BigDecimal bigDecimal4 = bigDecimal.setScale(2, RoundingMode.DOWN);
		System.out.println("DOWN==>"+bigDecimal4);
		//直接入
		BigDecimal bigDecimal6 = bigDecimal.setScale(2, RoundingMode.UP);
		System.out.println("UP==>"+bigDecimal6);

		BigDecimal bigDecimal2 = new BigDecimal("1.1");
		BigDecimal add = bigDecimal.add(bigDecimal2);
		System.out.println("add==>"+add);
		BigDecimal add1 = bigDecimal.divide(bigDecimal2,2,RoundingMode.HALF_UP);
		System.out.println(add1);

		Double dd = 1.23213d;
		BigDecimal bigDecimal5 = new BigDecimal(dd.toString());
		System.out.println(bigDecimal5.doubleValue());
	}
}
