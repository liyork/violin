package com.wolf.test.jvm.initialseq;

/**
 * Description: 静态初始化顺序测试
 * 实例初始化不一定要在类初始化完全结束之后才开始初始化。
 * 在实例化st变量时，实际上是把实例初始化嵌入到了静态初始化流程中，嵌入到了静态初始化的起始位置
 *
 * @author 李超
 * @date 2020/02/09
 */
public class StaticTest {

    public static void main(String[] args) {
        staticFunction();
    }

    static StaticTest st = new StaticTest();

    static {   //静态代码块
        System.out.println("1");
    }

    {       // 实例代码块
        System.out.println("2");
    }

    StaticTest() {    // 实例构造器
        System.out.println("3");
        System.out.println("a=" + a + ",b=" + b);
    }

    public static void staticFunction() {   // 静态方法
        System.out.println("4");
    }

    int a = 110;    // 实例变量
    static int b = 112;     // 静态变量
}
