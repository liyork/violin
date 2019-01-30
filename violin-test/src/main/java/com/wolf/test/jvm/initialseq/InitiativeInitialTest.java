package com.wolf.test.jvm.initialseq;

import com.wolf.test.jvm.initialseq.vo.SubClass;
import com.wolf.test.jvm.initialseq.vo.SuperClass;

/**
 * Description: 主动加载、被动加载
 *
 * <br/> Created on 11/1/17 7:25 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class InitiativeInitialTest {

    public static void main(String[] args) {
//        testOnlyLoadSubClass();
//        testRefParentValue();
        testSubClass();
//        testArray();
//        testStaticFinal();
//        testStaticFinal2();
    }

    private static void testOnlyLoadSubClass() {
        System.out.println(SubClass.class);
    }

    //使用的字段是静态字段，只初始化拥有这个字段的父类，但是子、父类Class都加载
    private static void testRefParentValue() {
        System.out.println(SubClass.a);
    }

    //主动使用子类静态字段，则触发初始化子类，也会初始化父类
    private static void testSubClass() {
        System.out.println(SubClass.b);
    }

    //仅仅定义SuperClass类型数组，不是主动使用，不会触发初始化,但是触发了加载Class
    //仅仅在内存中开辟了一段连续的地址空间4byte*10
    private static void testArray() {
        SuperClass[] superClasses = new SuperClass[10];
        SuperClass superClass = null;
    }

    //没有任何class加载
    //通过反编译可以看到，由于是常量，已被常量传播优化，直接放在了InitializationTest中的常量池中
    private static void testStaticFinal() {

        System.out.println(SuperClass.HELLO);
    }

    //由于加载、连接阶段不能计算出来值，所以只有初始化后才能得到结果。所以属于主动使用
    private static void testStaticFinal2() {

        System.out.println(SuperClass.RANDOM);
    }
}
