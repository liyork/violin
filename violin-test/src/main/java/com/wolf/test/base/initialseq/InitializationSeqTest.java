package com.wolf.test.base.initialseq;

/**
 * Description:
 * <br/> Created on 11/1/17 7:25 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class InitializationSeqTest {

    //-XX:+TraceClassLoading
    public static void main(String[] args) {
//        testOnlyLoadClass();
//        testRefParentValue();
        testSubClass();
//        testArray();
//        testStaticFinal();
    }

    private static void testOnlyLoadClass() {
        System.out.println(SubClass.class);
    }

    //使用的字段是静态字段，只初始化拥有这个字段的父类，但是子、父类都加载
    private static void testRefParentValue() {
        System.out.println(SubClass.a);
    }

    //主动使用子类静态字段则也会初始化父类
    private static void testSubClass() {
        System.out.println(SubClass.b);
    }

    //仅仅定义，不会触发初始化,但是触发了加载
    private static void testArray() {
        SuperClass[] superClasses = new SuperClass[10];
        SuperClass superClass = null;
    }

    //通过反编译可以看到，由于是常量，已被常量传播优化，直接放在了InitializationTest中的常量池中
    private static void testStaticFinal() {
        System.out.println(SuperClass.HELLO);
    }
}
