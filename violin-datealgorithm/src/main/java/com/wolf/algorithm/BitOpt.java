package com.wolf.algorithm;

import org.junit.Test;

/**
 * Description: 移位操作，左乘右除
 * 元素向左移动n位相当于乘以2^n
 *
 * @author 李超
 * @date 2020/01/06
 */
public class BitOpt {

    // m乘以2的n次方
    public static int powerN(int m, int n) {
        for (int i = 0; i < n; i++) {
            m = m << 1;
        }
        return m;
    }

    // n是否2的n次方
    // 若用%2或者不断移1位(即/2)判断则时间复杂度为O(logn)
    // 所有2^n的二进制中只有一位是1，其余都是0，即判断是否只有一位1，若只有一位1则-1后就没有1，所以私用&操作判断。
    public static boolean isPower(int n) {
        if (n < 1) {
            return false;
        }
        return ((n - 1) & n) == 0;
    }

    // 求二进制表示时1的个数。
    // 思路一：每次&1得到最后1位++，然后右移1位直到0,O(n)，n为二进制数的位数。
    // 思路二：n&(n-1)后少个1(因为-1需要借位，而&之后就把那位去掉了)，直到为0
    public static int countOneNum(int n) {
        int count = 0;
        while (n > 0) {
            count++;
            n = ((n - 1) & n);
        }
        return count;
    }

    @Test
    public void testPowerN() {
        int i = powerN(2, 3);
        System.out.println(i);
    }

    @Test
    public void testIsPower() {
        System.out.println(isPower(2));
        System.out.println(isPower(3));
        System.out.println(isPower(4));
    }

    @Test
    public void testCountOneNum() {
        System.out.println(countOneNum(1));
        System.out.println(countOneNum(2));
        System.out.println(countOneNum(5));
    }
}
