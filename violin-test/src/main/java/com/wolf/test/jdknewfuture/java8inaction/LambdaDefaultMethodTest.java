package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Description: default method test
 *
 * @author 李超
 * @date 2019/07/30
 */
public class LambdaDefaultMethodTest {

    @Test
    public void testStaticMethod() {
        List<Integer> list = Arrays.asList(2, 7, 3, 1);
        list.sort(Comparator.naturalOrder());
        System.out.println(list);
    }

    interface Sized {
        int size();

        //默认方法，免去子类强制实现方法的约束
        default boolean isEmpty() {
            return size() == 0;
        }
    }

    interface Moved {
        int getLeft();

        int setLeft(int x);

        default void move(int x) {
            setLeft(getLeft() + x);
        }
    }

    //组合多种默认实现,通过精简的接口，你能获得最有效的组合
    class TestCompose implements Sized, Moved {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public int getLeft() {
            return 0;
        }

        @Override
        public int setLeft(int x) {
            return 0;
        }
    }

    //多继承覆盖原则:
    //1.类中的方法优先级最高(要复写，不是仅仅从父类或者父接口继承而来)。类或父类中声明的方法的优先级高于任何声明为默认方法的接口的优先级。
    //2.上述无法判断，那么子接口(继承最下层)的优先级更高：函数签名相同时，优先选择拥有最具体实现的默认方法的接口，即如果B继承了A，那么B就比A更加具体。
    //3.上述无法判断，继承了多个接口的类必须通过显式覆盖和调用期望的方法显式地选择使用哪一个默认方法的实现。

    interface A {
        default void test() {
            System.out.println("a");
        }
    }

    interface B extends A {
        default void test() {
            System.out.println("b");
        }
    }

    //case1:B extends A,相对来说B具体
    static class C implements B, A {

        public static void main(String[] args) {
            new C().test();
        }
    }

    static class D implements A {
    }

    //case2:D中没重写，是从A继承来的test，B具体
    static class E extends D implements B, A {

        public static void main(String[] args) {
            new E().test();
        }
    }

    static class Q implements A {

        public void test() {
            System.out.println("q");
        }
    }

    //case4:Q重新声明，而且是类比B接口优先级高
    static class Z extends Q implements B, A {

        public static void main(String[] args) {
            new Z().test();
        }
    }

    interface A1 {
        default void test() {
            System.out.println("a1");
        }
    }

    //case5:I1无法判断是A还是A1
    static class I1 implements A, A1 {

        @Override
        public void test() {
            A.super.test();
        }

        public static void main(String[] args) {
            new I().test();
        }
    }

    interface H extends A {
    }

    interface H1 extends A {
    }

    //case5:菱形问题，H和H1都没有实现，结果只有A的默认实现
    static class I implements H, H1 {

        public static void main(String[] args) {
            new I().test();
        }
    }

    interface K1 extends A {
    }

    interface K extends A {
        /*abstract*/ void test();
    }

    //case5:K比K1和A都具体，需要重写K的抽象方法
    static class J implements K, K1 {

        @Override
        public void test() {
//            K.super.test();//抽象方法不能使用
//            K1.super.test();//方法已被K重写，不能使用
            System.out.println("I");
        }

        public static void main(String[] args) {
            new I().test();
        }
    }

}
