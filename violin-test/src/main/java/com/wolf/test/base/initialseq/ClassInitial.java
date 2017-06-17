package com.wolf.test.base.initialseq;

/**
 * <p> Description: 测试类初始化过程
 * <p/>
 * 类被使用前需要：
 * a.加载
 * b.链接(验证，准备--静态字段分配内存并给默认值，解析)
 * c.初始化--使用静态赋值
 * 使用new时，先执行abc，再调用构造器(内部先调用super),成员赋值(已经被编译到构造器中了)，构造方法其余部分
 * 注：类初始化时如果静态变量使用new，则先走实例化步骤先不管static,静态new完了再依次static
 * Date: 2015/10/28
 * Time: 11:13
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ClassInitial implements Cloneable {
    public static int k = 0;
    public static ClassInitial t1 = new ClassInitial("t1");
    public static ClassInitial t2 = new ClassInitial("t2");
    public static int i = print("i");
    public static int n = 99;

    public int j = print("j");

    {
        print("构造块");
    }

    static {
        print("静态块");
    }

    public ClassInitial(String str) {
        System.out.println((++k) + ":" + str + "    i=" + i + "  n=" + n);
        ++n;
        ++i;
    }

    public static int print(String str) {
        System.out.println((++k) + ":" + str + "   i=" + i + "   n=" + n);
        ++n;
        return ++i;
    }
}



