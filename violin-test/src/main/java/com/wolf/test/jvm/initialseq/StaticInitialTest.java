package com.wolf.test.jvm.initialseq;

import com.wolf.test.jvm.initialseq.vo.MyClass1;
import com.wolf.test.jvm.initialseq.vo.StaticConcurrentTest;

import java.lang.reflect.Field;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Description:测试静态属性/代码块，执行时机
 * <p>
 * <p>
 * <br/> Created on 2018/5/7 9:49
 *
 * @author 李超
 * @since 1.0.0
 */
public class StaticInitialTest {

    Class[] classArray = {
            MyClass1.class//这样引用该类，必然需要将该类加载到虚拟机中
    };//main中未创建对象，并不会调用这里


    //同样不会被"初始化"，因为MyClass1.class并未主动初始化
    static Class[] classArray1 = {
            MyClass1.class
    };


    static {
        x = 2;//能赋值下面的变量
//        System.out.println(x);//不能赋值
    }

    private static int x = 1;

    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
//        testNoInitial();
//        testInitial();
//        testWeatherLoadClass();
//        testConcurrent();
        testInitSql();
    }

    //都不会执行MyClass1的静态代码块,静态代码块的执行是处在类加载的最后一个阶段"初始化”，需要主动初始化
    private static void testNoInitial() throws ClassNotFoundException {
        System.out.println("hello word");
        System.out.println("hello word2 " + MyClass1.class);
        Class.forName("com.wolf.test.jvm.initialseq.vo.MyClass1", false,
                StaticInitialTest.class.getClassLoader());
    }

    //都是主动初始化
    private static void testInitial() throws ClassNotFoundException {

        //int a = MyClass1.a;//使用字段

        //第二个参数true才会加载并"初始化"调用静态块
        Class.forName("com.wolf.test.jvm.initialseq.vo.MyClass1", true, StaticInitialTest.class.getClassLoader());
    }

    //加载了MyClass1，但是没有初始化，MyClass1.class不属于主动初始化
    private static void testWeatherLoadClass() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        printLoadedClass(loader);
        System.out.println("-------------------- hello " + MyClass1.class + " --------------------");
        printLoadedClass(loader);
    }

    private static void printLoadedClass(ClassLoader loader) {

        try {
            Field classesF = ClassLoader.class.getDeclaredField("classes");
            classesF.setAccessible(true);
            Vector<Class<?>> classes = (Vector<Class<?>>) classesF.get(loader);
            for (Class c : classes) {
                System.out.println("loadclass:" + c);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void testConcurrent() throws InterruptedException {

        IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " is running ");
                new StaticConcurrentTest();
            }).start();
        });

        TimeUnit.SECONDS.sleep(11111);
    }

    //主动初始化：
    //准备-默认值，依次调用：staticConcurrentTest=对象,x=1,y=1
    //初始化-使用用户设定的赋值：staticConcurrentTest=对象,x=0,y=1
    private static void testInitSql() throws InterruptedException {

        System.out.println(StaticConcurrentTest.x+" "+StaticConcurrentTest.y);
    }

}


