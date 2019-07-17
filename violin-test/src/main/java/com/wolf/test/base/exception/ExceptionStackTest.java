package com.wolf.test.base.exception;

import org.junit.Test;

/**
 * <p> Description:
 * 每次throw一个异常，会调用fillInStackTrace(0);向栈中压入异常
 * 每次捕获后再抛出异常还是会调用fillInStackTrace(0);向栈中压入异常
 * <p>
 * e.printStackTrace  -- Throwable.printStackTrace -- s.println(this); -- PrintStream.println -- String.valueOf(x);
 * Throwable.toString -- getLocalizedMessage -- getMessage detailMessage
 * 然后再打印异常栈信息
 * logger.error -- new ThrowableProxy -- throwable.getMessage()
 * 打印堆栈信息
 * <p>
 * 异常栈以FILO的顺序打印，位于打印内容最下方的异常最早被抛出，逐渐导致上方异常被抛出。
 * 从上到下数，第2个异常是第1个异常被抛出的原因cause，以“Caused by”开头，表示被包装.
 * <p>
 * 异常栈中每个异常打印信息由：异常名+细节信息+回车+路径组成
 * 路径以FIFO的顺序打印，位于打印内容最上方的位置最早被该异常经过，逐层向外抛出。
 * <p>
 * new RuntimeException时会调用fillInStackTrace(0)，即把自己当前所在方法stack frames放入```自己的```栈中。
 * 那么当e.printStackTrace()时，先s.println(this)，打印自己即toString(getClass().getName():message),
 * 再打印getOurStackTrace这个是fillInStackTrace放入的(FIFO)，即从异常触发点开始倒叙调用路线，再getSuppressed，
 * 再getCause并打印(即包装的异常)，这个是个递归，一直到getCause么有了，可能一堆causeby
 * <p>
 * 所以看到异常信息是：```当前异常对象```的栈的getClass().getName():message，然后是最外层抛出异常开始倒叙调用，
 * 然后是causeby即包装的异常，然后是倒叙打印行，然后再下causeby。
 * <p>
 * 打印的都是某个异常对象的堆栈逆序信息。
 *
 * <p>
 * <p/>
 * Date: 2016/5/25
 * Time: 17:35
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class ExceptionStackTest {

    //先打印异常的信息，然后栈是最发生的先打印，然后打印调用方法
    @Test
    public void test1Stack() {
        try {
            test1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test1() {
        System.out.println("1111");
        throw new RuntimeException("xxx111");
    }

    //只有一个异常，所以你需打印，没有caused by
    @Test
    public void test2_0Stack() {
        try {
            test2_0();
        } catch (Exception e) {
            System.out.println("print invoke test2_0 error stack");
            e.printStackTrace();//打印e的堆栈逆序被调用信息
        }
    }

    private static void test2_0() {
        System.out.println("2222_0");
        test3_0();
    }

    private static void test3_0() {
        System.out.println("333_0");
        throw new RuntimeException("xxx333_0");
    }

    //因为test2方法调用test3时出现的异常被包装重新抛出了，即重新放入了异常栈，所以打印时先打印最近抛出的异常，然后是cause by
    @Test
    public void test2Stack() {
        try {
            test2();
        } catch (Exception e) {
            System.out.println("print invoke test2 error stack");
            e.printStackTrace();//打印e的堆栈逆序被调用信息
        }
    }

    private static void test2() {
        System.out.println("2222");
        try {
            test3();
        } catch (Exception e) {
            //System.out.println("print invoke test3 error stack");
            e.printStackTrace();//打印的是e的堆栈信息，即e开始被抛出时的逆序信息
            throw new RuntimeException("xxx222", e);//再包装并抛出
        }
    }

    private static void test3() {
        System.out.println("333");
        throw new RuntimeException("xxx333");
    }

    //错误示范！！！
    @Test
    public void test4Stack() {
        try {
            test4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test4() {
        System.out.println("4444");
        try {
            test5();
        } catch (Exception e) {
            throw new RuntimeException("xxx444", e);
        }

    }

    private static void test5() {
        System.out.println("5555");
        throw new ErrorExceptionExample("xxx5555");
    }

    @Test
    public void testStackPrinciple() {
        try {
            test6();
        } catch (Exception e) {
            //先打印异常tostring，然后获取当前栈帧中的信息，反向打印，然后若是有cauesed by则获取cauesd异常并打印
            e.printStackTrace();
        }
    }

    private static void test6() {
        try {
            test7();
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("test7 error ", e);
        }
    }

    //方法每调用则压入栈帧，执行完则出栈
    private static void test7() throws Exception {
        throw new Exception("xxxa bc ");//构造时，fillInStackTrace(0)将当前异常信息放入栈帧
    }
}

