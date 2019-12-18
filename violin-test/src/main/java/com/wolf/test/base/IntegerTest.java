package com.wolf.test.base;

/**
 * Description: integer不可变
 *
 * @author 李超
 * @date 2019/12/07
 */
public class IntegerTest {

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        b = a;
        // integer不可变，导致++会将新值2赋予b
        Integer c = b++;
        System.out.println(a + "_" + b + "_" + c);
    }
}
