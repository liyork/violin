package com.wolf.test.base;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/12/07
 */
public strictfp class StrictFpTest {

    public static void main(String[] args) {
        float f = 0.12365f;
        double d = 0.03496421d;
        double sum = f + d;
        System.out.println(sum);
    }
}
