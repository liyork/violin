package com.wolf.test.base;

/**
 * Description:
 *
 * @author 李超
 * @date 2020/02/08
 */
public class StringAddTest {

    public static void main(String[] args) {
        String str3 = "what";
        String str4 = str3 + " a nice day";
        System.out.println("what a nice day" == str4);
        System.out.println("what a nice day" == str4.intern());
    }
}
