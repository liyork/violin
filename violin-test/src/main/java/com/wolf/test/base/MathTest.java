package com.wolf.test.base;

import org.junit.Test;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/12/07
 */
public class MathTest {

    // 坐标轴  ------ 0 ++++++
    // 正数，四舍五入。负数，六以上向左移动，五及以下向右移动
    @Test
    public void testRound() {
        System.out.println(Math.round(1.4));
        System.out.println(Math.round(1.5));
        System.out.println(Math.round(1.6));

        System.out.println(Math.round(-1.6));
        System.out.println(Math.round(-1.5));
        System.out.println(Math.round(-1.4));
        System.out.println(Math.round(-11.5));
    }

    // 正的小数向上取整，负的小数舍弃
    @Test
    public void testCeil() {
        System.out.println(Math.ceil(1.1));
        System.out.println(Math.ceil(1.5));
        System.out.println(Math.ceil(1.6));

        System.out.println(Math.ceil(-1.8));
        System.out.println(Math.ceil(-1.5));
        System.out.println(Math.ceil(-1.4));
        System.out.println(Math.ceil(-11.5));
    }

    // 正的小数舍弃，负的小数向左
    @Test
    public void testFloor() {
        System.out.println(Math.floor(1.1));
        System.out.println(Math.floor(1.5));
        System.out.println(Math.floor(1.6));

        System.out.println(Math.floor(-1.8));
        System.out.println(Math.floor(-1.5));
        System.out.println(Math.floor(-1.4));
        System.out.println(Math.floor(-11.5));
    }
}
