package com.wolf.test.base;

/**
 * <p> Description:
 *
 * sublen = value.length - beginIndex
 * subLen = endIndex - beginIndex
 * 所以我们endIndex一般比要取的下表大1
 * endIndex不被包含，但是一定要sublen>1才能出来数据
 * <p/>
 * Date: 2016/5/3
 * Time: 13:58
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class StringIndexTest {

	public static void main(String[] args) {
		String test = "ht?tp://www.brainysoftware.com/index.html?name=Tarzan";
		System.out.println(test.length());
		int firstIndex = test.indexOf("://");//  ://中:冒号的位置,从0开始
		System.out.println(firstIndex);
		System.out.println(test.indexOf("?"));//第一个?
		System.out.println(test.indexOf("?",firstIndex+3));//从://之后开始查找

		String keyValue = test.substring(test.lastIndexOf("?") + 1);
		System.out.println(keyValue);
		int equalMark = keyValue.indexOf("=");
		System.out.println(equalMark);
		//substring include fromIndex and exclude endIndex
		System.out.println(keyValue.substring(0,equalMark));
		System.out.println(keyValue.substring(equalMark+1,keyValue.length()));

		System.out.println(test.substring(1,1));
	}
}
