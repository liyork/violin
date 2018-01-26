package com.wolf.test.base;

/**
 * Description:
 * finaly。目的是给程序一个补救的机会
 * <p>
 * <br/> Created on 2017/7/4 13:59
 *
 * @author 李超
 * @since 1.0.0
 */
public class ReturnFinalTest {

    public static void main(String[] args) {
//        System.out.println(test1());
//        System.out.println(test2());
        System.out.println(test3().a);
        System.out.println(test4().a);
    }

    public static int test1() {
        System.out.println("11");

        try {
            return 1;//先放入罐子里，执行finally然后再从罐子里取返回
        } finally {
            System.out.println("2222");
        }
    }

    public static int test2() {
        System.out.println("11");

        try {
            return 1;//先放入罐子里，执行finally碰到有返回，则使用那个返回
        } finally {
            return 2;
        }
    }

    public static InnerClass test3() {
        System.out.println("11");

        InnerClass innerClass = new InnerClass();
        innerClass.a = 2;
        try {
            innerClass.a = 3;
            return innerClass;//罐子里是对象，finally里修改了对象
        } finally {
            innerClass.a = 4;
        }
    }

    public static InnerClass test4() {
        System.out.println("11");

        InnerClass innerClass = new InnerClass();
        innerClass.a = 2;
        try {
            innerClass.a = 3;
            return innerClass;
        } finally {
            innerClass.a = 4;
            return innerClass;
        }
    }

    static class InnerClass {
        int a;
    }
}
