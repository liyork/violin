package com.wolf.algorithm;

/**
 * Description: 测试index，+1，-1或者比较等场景
 *
 * @author 李超
 * @date 2020/01/01
 */
public class IndexTest {

    public static void main(String[] args) {

        int length = 10;
        for (int i = 0; i < length; i++) {// 从0开始，到length-1位置，一共循环length次
            System.out.print(i + " ");
        }

        System.out.println();

        for (int i = length - 1; i >= 0; i--) {// 从length-1开始，到0位置，一共循环length次
            System.out.print(i + " ");
        }

        System.out.println();

        for (int i = 0; i < length - 1; i++) {// 从0开始，到length-2位置，一共循环length-1次，因为涉及向后多比较一个值所以要提前结束
            System.out.print(i + "" + (i + 1) + " ");
        }

        System.out.println();

        // 求总数
        int aIndex = 7;
        int bIndex = 15;
        int num = (bIndex - aIndex) + 1;
        System.out.println("num:" + num);

        // 求中间索引
        int midIndex = (aIndex + bIndex) / 2;
        System.out.println("midIndex:" + midIndex);

        // 求中间索引-另一种算法?
        int midIndex2 = aIndex + (bIndex - aIndex) / 2;
        System.out.println("midIndex2:" + midIndex2);

        System.out.println();
    }
}
