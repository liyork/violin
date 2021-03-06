package com.wolf.test.base.reflect;

/**
 * Description:
 * <br/> Created on 2016/10/27 14:48
 *
 * @author 李超()
 * @since 1.0.0
 */
public class B implements A {

    public String name1;

    private String name;

    @Override
    public void test() {

    }

    private int test1() {
        return 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printStackTraceElement() {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stack) {
            System.out.println(stackTraceElement.getClassName() + "," + stackTraceElement.getMethodName());
        }
    }
}
