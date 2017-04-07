package com.wolf.test.base;

/**
 * Description:
 * 推荐使用内部类方式1
 * 如使用双重检查需保证是对的方式4
 * 3是在jdk1.5之前有问题，之后修复了
 * <br/> Created on 2016/8/11 16:52
 *
 * @author 李超()
 * @since 1.0.0
 */
public class SingletonTest {

    private SingletonTest() {
        System.out.println("construct...");
    }

    public static SingletonTest getInstance() {
        return SingletonTestHolder.instance;
    }

    //1.第一次被使用时才会被jvm加载而且jvm保证内部类加载线程安全
    private static class SingletonTestHolder {
        //final 类型的域是不能修改的（但如果 final 域所引用的对象时可变的，那么这些被引用的对象是可以修改的）。
        // 然而，在 Java 内存模型中，final 域还有着特殊的语义。final 域能确保初始化过程的安全性，
        // 从而可以不受限制的访问不可变对象，并在共享这些对象时无需同步。
        //在并发当中，原理是通过禁止cpu的指令集重排序,来保证对象的安全发布，防止对象引用被其他线程在对象被完全构造完成前拿到并使用。
        //与前面介绍的锁和volatile相比较，对final域的读和写更像是普通的变量访问。对于final域，编译器和处理器要遵守两个重排序规则：
        //1. 在构造函数内对一个final域的写入，与随后把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。
        //2. 初次读一个包含final域的对象的引用，与随后初次读这个final域，这两个操作之间不能重排序。
        private final static SingletonTest instance = new SingletonTest();
    }

    public static void main(String[] args) {
        System.out.println("111");
        //other class
        SingletonTest instance = SingletonTest.getInstance();
        System.out.println("222");
        System.out.println(instance);
    }


    private static SingletonTest instance = null;

    //2.防止由于构造SingletonTest时未完全就被其他线程使用了。//可能也不管用。。。
    public static SingletonTest getInstanceRight() {
        if(instance == null) {
            synchronized(SingletonTest.class) {
                SingletonTest temp = instance;
                if(temp == null) {
                    temp = new SingletonTest();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    //3.由于构造SingletonTest时未完全有可能被其他线程使用了。
    public static SingletonTest getInstanceError() {
        if(instance == null) {
            synchronized(SingletonTest.class) {
                if(instance == null) {
                    instance = new SingletonTest();
                }
            }
        }
        return instance;
    }

    //4.getInstanceError方法+volatile SingletonTest
}
