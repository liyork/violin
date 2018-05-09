package com.wolf.test.base;

import java.lang.reflect.Field;
import java.util.Vector;

/**
 * Description:测试静态属性/代码块，执行时机
 * <p>
 * 加载bean步骤：
 * 装载(通过名称产生二进制流，解析二进制流成为方法区内部结构，构造一个改类型的java.lang.Class类的实例)
 * 连接(验证，准备-分配初始默认值，解析-符号引用替换成直接引用(可选))
 * 初始化(主动使用时，创建新实例、调用静态方法、使用静态字段、调用某些反射方法、初始化子类、虚拟机启动包含main的启动类),收集所有静态初始化放入clinit
 * <br/> Created on 2018/5/7 9:49
 *
 * @author 李超
 * @since 1.0.0
 */
public class StaticTest {

//    Class[] classArray = {
//            MyClass1.class//这样引用该类，必然需要将该类加载到虚拟机中
//    };//main中未创建对象，并不会调用这里


    //同样不会被"初始化"
//    static Class[] classArray = {
//            MyClass1.class
//    };

    public static void main(String[] args) throws ClassNotFoundException {
//        testNoInitial();
//        testInitial();

        testWeatherLoadClass();
    }

    private static void testNoInitial() throws ClassNotFoundException {
        //都不会执行静态代码块,静态代码块的执行是处在类加载的最后一个阶段"“初始化”"
        System.out.println("hello word");
        System.out.println("hello word2 " + MyClass1.class);
        Class.forName("com.wolf.test.base.MyClass1", false, StaticTest.class.getClassLoader());
    }

    private static void testInitial() throws ClassNotFoundException {
        System.out.println("22222");
        //这句才会加载并““"初始化"调用静态块
        Class.forName("com.wolf.test.base.MyClass1", true, StaticTest.class.getClassLoader());
    }

    private static void testWeatherLoadClass() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        printClassesOfClassLoader(loader);
        System.out.println("-------------------- hello " + MyClass1.class + " --------------------");
        printClassesOfClassLoader(loader);
    }

    private static void printClassesOfClassLoader(ClassLoader loader) {

        try {
            Field classesF = ClassLoader.class.getDeclaredField("classes");
            classesF.setAccessible(true);
            Vector<Class<?>> classes = (Vector<Class<?>>) classesF.get(loader);
            for (Class c : classes) {
                System.out.println(c);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}


