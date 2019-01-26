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
public class StringTest {

	public static void main(String[] args) {
//		testIndex();
//		testHashcode();

//		testIntern1();
		testIntern2();
	}

	private static void testHashcode() {
		String str = "abc";
		System.out.println(str.hashCode());//循环每个字符操作：hashcode(i)=hashcode(i-1)*33+char(i)
	}

	private static void testIndex() {
		String test = "ht?tp://www.brainysoftware.com/index.html?name=Tarzan";
		System.out.println(test.length());
		int firstIndex = test.indexOf("://");//  ://中:冒号的位置,结果从0开始
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

	//1.7 intern只会将堆中存在的对象引用拷贝一份到方法区中，"计算机软件"构造stringbuilder后和方法区中是一个引用，
	// 而"java"早就存在方法区中，所以和新的stringbuilder不是一个引用，是通过StringBuilder新建的对象
	public static void testIntern1(){
		String str1 = new StringBuilder("计算机").append("软件").toString();
		System.out.println(str1.intern() == str1);

		String str2 = new StringBuilder("ja").append("va").toString();
		System.out.println(str2.intern() == str2);
	}

	//看来一旦有拼接的动作，似乎jvm也不会进行优化了呢。。
    // 通过javap -v StringTest，发现有构造java/lang/StringBuilder然后append/toString看来肯定不一样了
	public static void testIntern2(){
		long a = 1;
		String b = "b";
		String key1 = getKey(1, b);
		String key2 = getKey(1, b);
		System.out.println("key1==key2=>"+key1==key2);

		String c1 = a + "_" + b;
		String c2 = a + "_" + b;
		System.out.println("c1==c2=>"+c1==c2);//false
		System.out.println("c1-->"+c1.getClass());
		System.out.println("c2-->"+c2.getClass());
	}

	public static String getKey(long a, String b) {
//		return a + "_" + b;//false
		return b;//是true，看来都从堆中寻找
	}
}
