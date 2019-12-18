package com.wolf.test.base;

/**
 * Description:多态测试
 *
 * @author 李超
 * @date 2019/12/07
 */
public class PolymorphicTest {

    public static void main(String[] args) {
        ParentA a = new SubA();
        a.test("111");
        // 成员变量没有多态的概念，编译其就确定了
        System.out.println(a.a);
        SubA a1 = (SubA) a;
        System.out.println(a1.a);
    }
}

class ParentA {
    public int a = 1;

    public void test(String s) {
        System.out.println("111ParentA");
    }
}

class SubA extends ParentA {
    public int a = 2;

    @Override
    public void test(String s) {
        System.out.println("111SubA");
    }
}
