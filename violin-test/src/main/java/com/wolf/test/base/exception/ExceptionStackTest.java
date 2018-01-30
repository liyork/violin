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

//        try {
//            test2();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            test4();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            throw new RuntimeException("xxx222", e);
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


}

