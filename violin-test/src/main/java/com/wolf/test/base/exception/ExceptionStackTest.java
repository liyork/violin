package com.wolf.test.base.exception;

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
 *
 * 异常栈以FILO的顺序打印，位于打印内容最下方的异常最早被抛出，逐渐导致上方异常被抛出。
 * 从上到下数，第2个异常是第1个异常被抛出的原因cause，以“Caused by”开头，表示被包装.
 *
 * 异常栈中每个异常打印信息由：异常名+细节信息+回车+路径组成
 * 路径以FIFO的顺序打印，位于打印内容最上方的位置最早被该异常经过，逐层向外抛出。
 *
 * new RuntimeException时会调用fillInStackTrace(0)，即把自己当前所在方法stack frames放入```自己的```栈中。
 * 那么当e.printStackTrace()时，先s.println(this)，打印自己即toString(getClass().getName():message),
 * 再打印getOurStackTrace这个是fillInStackTrace放入的(FIFO)，即从异常触发点开始倒叙调用路线，再getSuppressed，
 * 再getCause并打印(即包装的异常)，这个是个递归，一直到getCause么有了，可能一堆causeby
 *
 * 所以看到异常信息是：```当前异常对象```的栈的getClass().getName():message，然后是最外层抛出异常开始倒叙调用，
 * 然后是causeby即包装的异常，然后是倒叙打印行，然后再下causeby。
 *
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

    public static void main(String[] args) {

//        try {
//            test1();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            test2();
        } catch (Exception e) {
            e.printStackTrace();//打印e的堆栈逆序被调用信息
        }


//        try {
//            test4();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //test6();

    }


    private static void test1() {
        System.out.println("1111");
        throw new RuntimeException("xxx111");
    }

    private static void test2() {
        System.out.println("2222");
        try {
            test3();
        } catch (Exception e) {
            e.printStackTrace();//打印的是e的堆栈信息，即e开始被抛出时的逆序信息
            throw new RuntimeException("xxx222", e);//再包装并抛出
        }

    }

    private static void test3() {
        System.out.println("333");
        throw new RuntimeException("xxx333");
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

    private static void test6() {
        Exception exception = new Exception("xxxa bc ");//构造时，fillInStackTrace(0)
        exception.printStackTrace();
    }

}

