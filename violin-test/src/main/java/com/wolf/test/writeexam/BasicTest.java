package com.wolf.test.writeexam;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2017/4/22 18:16
 *
 * @author 李超
 * @since 1.0.0
 */
public class BasicTest extends SuperClass {

    //final native getClass,不可继承，不论调用super和this都是用的父类的方法，获取的是运行时的真正对象
    @Test
    public void testGetClassName() {
        System.out.println(super.getClass().getName());
        System.out.println(this.getClass().getSimpleName());
        //获取父类的名称
        System.out.println(this.getClass().getSuperclass().getName());
        System.out.println(this.getClass().getClass());

    }


    public static void test() {
        System.out.println("test...");
    }

    //null可以被强制转换成任意类型(BasicTest)null
    //null不能调用对象方法，但是可以调用类方法,类方法不依赖于对象
    @Test
    public void testStaticMethod() {
        ((BasicTest) null).test();
    }

    //获取对象三种方法
    @Test
    public void testGetClassWay() throws ClassNotFoundException {
        Class<BasicTest> x1 = BasicTest.class;
        System.out.println(x1);
        System.out.println(new BasicTest().getClass());
        Class<BasicTest> basicTestClass = (Class<BasicTest>) Class.forName("com.wolf.test.writeexam.BasicTest");
        System.out.println(basicTestClass);
    }

    //每个种类的对象都对应唯一的一个class对象用来生成对象，每个class对象的getclass都是统一的Class对象
    //这里仅仅试验，正规化下面代码通过ctrl+2可以得到泛型class就不能用==判断了
    @Test
    public void testGetClassInternal() throws ClassNotFoundException {
        Class basicTestClass = BasicTest.class;
        Class aClass1 = new BasicTest().getClass();
        System.out.println(basicTestClass == aClass1);

        //最终class对象
        System.out.println(ByteOrderTest.class.getClass());
        System.out.println(ByteOrderTest.class.getClass().getName());
        System.out.println(basicTestClass.getClass().getName());

        Class aClass = new ByteOrderTest().getClass();
        System.out.println(aClass == aClass1);
        System.out.println(ByteOrderTest.class.getClass() == basicTestClass.getClass());
    }


    @Test
    public void testJDK7NewFeature() throws ClassNotFoundException {
        //先用string的hascode定位到case，然后再用equals
        String a = "1";
        switch(a) {
            case "a":
            case "b":

        }

        //支持多catch
        int b = 2;
        if(b == 1) {
            try {
                throw new InterruptedException("xxx");
            } catch (InterruptedException | RuntimeException e) {
                e.printStackTrace();
            }
        }

        //支持二进制字面量
        int c = 0b001;
        //仅数字支持下划线，编译后去除，用来查看方便
        int d = 10_000_000;

        //类型推测
        Map<String, Integer> map = new HashMap<>();

        //try-with-resources 编译代码时自动加上final关闭资源语句
        try(InputStream fis = new FileInputStream("D:\\a.txt");) {
            while(fis.read() != -1) System.out.println(fis.read());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //fork/join
    }

    //private修饰的属性，不能被实例变量使用，不能为子类继承、使用


    @Test
    public void testReturnInMiddle() throws ClassNotFoundException {
        System.out.println(test1());
        System.out.println(test2());
    }

    private static int test1() {
        int x = 1;
        try {
            //先放在罐子里，执行finally，然后再返回结果
            return x;
        } finally {
            ++x;
        }
    }

    int test2() {
        try {
            return func1();
        } finally {
            //当finnaly中也有return时将把try中的return结果覆盖
            return func2();
        }
    }

    private int func1() {
        System.out.println("func1");
        return 1;
    }

    private int func2() {
        System.out.println("func2");
        return 2;
    }

}


