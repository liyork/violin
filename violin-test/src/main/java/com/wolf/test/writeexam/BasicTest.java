package com.wolf.test.writeexam;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <br/> Created on 2017/4/18:16
 *
 * @author 李超
 * @since 1.0.0
 */
public class BasicTest extends SuperClass {

    //final native getClass,不可重写，不论调用super和this都是用的Object的方法，获取的是运行时的真正对象
    @Test
    public void testGetClassName() {
        System.out.println(super.getClass().getName());
        System.out.println(this.getClass().getSimpleName());
        // 正确获取父类的名称
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
        switch (a) {
            case "a":
            case "b":

        }

        //支持多catch
        int b = 2;
        if (b == 1) {
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
        try (InputStream fis = new FileInputStream("D:\\a.txt");) {
            while (fis.read() != -1) System.out.println(fis.read());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //fork/join
    }

    //private修饰的属性，不能被实例变量使用，不能为子类继承、使用

    // finally块的作用就是为了保证无论出现什么情况，finally块里的代码一定被执行。
    // 程序执行return就意味着结束对当前函数的调用并跳出这个函数体，因此任何语句要执行都只能在return前(除非exit)，finally也一样。
    @Test
    public void testReturnInMiddle() throws ClassNotFoundException {
//        System.out.println(testFinally1());
//        System.out.println(testFinally2());
//        System.out.println(testFinally3());
//        System.out.println(testFinally4());
//        testFinally5();
        testFinally6();
    }

    // 测试return和finally执行顺序
    private static int testFinally1() {
        try {
            return 1;
        } finally {
            System.out.println("exe finally");
        }
    }

    // 测试return返回值
    private static int testFinally2() {
        int x = 1;
        try {
            return x;
        } finally {
            // 会覆盖其他return语句。
            return 3;
        }
    }

    // 测试finally对return的值(基本类型)影响
    private static int testFinally3() {
        int x = 1;
        try {
            // 执行到return时先将返回值存储在一个指定位置，然后执行finally块，最后返回值。
            return x;
        } finally {
            ++x;
        }
    }

    // 测试finally对return的值(引用类型)影响
    private static StringBuilder testFinally4() {
        StringBuilder x = new StringBuilder("1");
        try {
            // 执行到return时先将返回值存储在一个指定位置，然后执行finally块，最后返回值。finally中对指定引用修改了。
            return x;
        } finally {
            x.append("abc");
        }
    }

    // try之前出现异常，不执行finally
    private static void testFinally5() {
        int i = 5 / 0;
        try {
            System.out.println("try block");
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
        }
    }

    private static void testFinally6() {
        try {
            System.out.println("try block");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
        }
    }

    public static void main(String[] args) {
//        System.out.println(getInt());
        System.out.println(getInt2());
    }

    public static int getInt() {
        int a = 10;
        try {
            System.out.println(a / 0);
            a = 20;
        } catch (ArithmeticException e) {
            a = 30;
            return a;
            /*
             * return a 在程序执行到这一步的时候，这里不是return a 而是 return 30；这个返回路径就形成了
             * 但是呢，它发现后面还有finally，所以继续执行finally的内容，a=40
             * 再次回到以前的路径,继续走return 30，形成返回路径之后，这里的a就不是a变量了，而是常量30
             */
        } finally {
            a = 40;
        }

        return a;
    }

    public static int getInt2() {
        int a = 10;
        try {
            System.out.println(a / 0);
            a = 20;
        } catch (ArithmeticException e) {
            a = 30;
            return a;
            // 与上述一致
        } finally {
            a = 40;
            return a; //如果这样，就又重新形成了一条返回路径，由于只能通过1个return返回，所以这里直接返回40
        }
    }
}


