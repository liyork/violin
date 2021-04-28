package com.wolf.test.base;

import org.junit.Test;

/**
 * Description:
 *
 * @author 李超
 * @date 2021/01/11
 */
public class StringBuilderTest {

    @Test
    public void testReverse() {
        StringBuilder s = new StringBuilder("abcde");
        s.reverse();
        System.out.println(s);
    }

    @Test
    public void testClear() {
        StringBuilder s = new StringBuilder("abcde");
        System.out.println(s);
        s.setLength(0);
        System.out.println(s);
    }
}
