package com.wolf.test.base;

/**
 * <p> Description: 测试类初始化过程
 * <p/>
 * 类被使用前需要：
 * a.加载 b.链接(验证，准备--静态字段分配内存并给默认值，解析) c.初始化--使用静态赋值
 * d.使用时new，先为对象和实例字段分配内存并初始默认值，再调用构造器(内部先调用super),成员赋值，构造方法其余部分
 * 注：类初始化时如果静态变量使用new，则先调用d,然后依次static
 * Date: 2015/10/28
 * Time: 11:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ClassInitialTest implements Cloneable {
	public static int k = 0;
	public static ClassInitialTest t1 = new ClassInitialTest("t1");
	public static ClassInitialTest t2 = new ClassInitialTest("t2");
	public static int i = print("i");
	public static int n = 99;

	public int j = print("j");

	{
		print("构造块");
	}

	static {
		print("静态块");
	}

	public ClassInitialTest(String str) {
		System.out.println((++k) + ":" + str + "    i=" + i + "  n=" + n);
		++n;
		++i;
	}

	public static int print(String str) {
		System.out.println((++k) + ":" + str + "   i=" + i + "   n=" + n);
		++n;
		return ++i;
	}

	public static void main(String[] args) {
		new ClassInitialTest("init");
	}

}


class Init {
	public static void main(String[] args) throws Exception {
		S s = new S();
		System.out.println(s.getV2());
	}
}

class P {
	private int v1 = 5;
	private int v2 = getV1();

	public P() throws Exception {
		System.out.println("P");
	}

	public int getV1() {
		return v1;
	}

	public int getV2() {
		return v2;
	}
}

class S extends P {
	private int value1 = 4;

	public int getV1() {
		return value1;
	}

	public S() throws Exception {
		this("S()");
	}

	public S(String msg) throws Exception {
		System.out.println(msg);
	}

	public S(int v) throws Exception {
		super();
		System.out.println("abc");
	}
}