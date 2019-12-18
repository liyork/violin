package com.wolf.test.base;

import org.junit.Test;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/12/07
 */
public class IAddAddTest {

    @Test
    public void testIAddAdd() {

        int i = 1;
        // 执行第一个i++，此时i=1，执行后i=2，执行第二个i++，此时i=2，执行后i=3，再计算1+2得到3
        System.out.println(i++ + i++);
        System.out.println(i);
        // 执行第一个i++，此时i=3，执行后i=4，执行第二++i，执行后i=5，在计算3+5得到8
        System.out.println((i++) + (++i));
        System.out.println(i);
        // 第一个i++，此时i=5，执行后i=6，第二i++，此时i=6，执行后i=7，第三个i++，此时i=7，执行后i=8，在执行5+6+7=18
        System.out.println(i++ + i++ + i++);
        System.out.println(i);
    }
}
